import korlibs.event.*
import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.image.tiles.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.tiles.*
import korlibs.math.geom.*
import korlibs.math.geom.slice.*

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo { MyScene() }
}

class MyScene : Scene() {
    private val tileSize = 16
    private val screenSize = 32
	override suspend fun SContainer.sceneMain() {
        val tilesBmp = resourcesVfs["gfx/tiles.ase"].readBitmap(ASE) // getting tiles as bitmap
        val tileSet = TileSet(tilesBmp.slice(), tileSize, tileSize) //slicing the bitmap and creating a tile set
        val tileMap = tileMap(TileMapData(screenSize,screenSize, tileSet = tileSet)) // actual screen with data
        // creating the walls
        creatingWalls(tileMap)
        // creating the snake
        val snake = Snake(listOf(SnakePiece(PointInt(screenSize/2, screenSize/2),true), SnakePiece(PointInt(screenSize/2 + 1,screenSize/2))))
        snake.render(tileMap.map)
	}

    private fun creatingWalls(tileMap: TileMap) {
        for (y in 0 until screenSize) {
            for (x in 0 until screenSize) {
                val wallOrientation = when {
                    x == 0  && y == 0 -> SliceOrientation.NORMAL.flippedX()
                    x == 0 && y == screenSize - 1 -> SliceOrientation.NORMAL.flippedY().flippedX()
                    x == screenSize - 1 && y == screenSize - 1 -> SliceOrientation.NORMAL.flippedY()
                    y > 0 && y < screenSize - 1 -> SliceOrientation.NORMAL.rotatedRight()
                    else -> SliceOrientation.NORMAL
                }
                tileMap.map[x,y] = when {
                    (x == 0 || x == screenSize - 1) && (y == 0 || y == screenSize - 1) -> Tile(19, wallOrientation)
                    (x == 0 || x == screenSize - 1) || (y == 0 || y == screenSize - 1) -> Tile(18, wallOrientation)
                    else -> Tile.INVALID
                }
            }
        }
    }

}

enum class SnakeDirection(
    val delta: Vector2I
) {
    UP(Vector2I(0,-1)),
    DOWN(Vector2I(0,+1)),
    LEFT(Vector2I(-1,0)),
    RIGHT(Vector2I(+1,0))
}

data class Snake(
    val pieces: List<SnakePiece>
) {

    fun withMove(dir: SnakeDirection) : Snake {
        val lastPiece = pieces.last()
        val newPiece = lastPiece.copy(
            position = lastPiece.position + dir.delta
        )
        return Snake(pieces + newPiece)
    }

    fun render(map: TileMapData){
        for (piece in pieces){
            map[piece.position.x,piece.position.y] = if (piece.isHead) Tile(0) else Tile(2)
        }
    }
}

data class SnakePiece(
    val position: PointInt,
    val isHead: Boolean = false
)
