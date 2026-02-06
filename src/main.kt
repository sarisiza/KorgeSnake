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
import snake.*
import walls.*

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo { MyScene() }
}

class MyScene : Scene() {
    private val tileSize = 16
    private val screenWidth = 32
    private val screenHeight = 32
    private val wallProvider : WallsProvider = WallsProviderImpl()
	override suspend fun SContainer.sceneMain() {
        val tilesBmp = resourcesVfs["gfx/tiles.ase"].readBitmap(ASE) // getting tiles as bitmap
        val tileSet = TileSet(tilesBmp.slice(), tileSize, tileSize) //slicing the bitmap and creating a tile set
        val tileMap = tileMap(TileMapData(screenWidth,screenHeight, tileSet = tileSet)) // actual screen with data
        // creating the walls
        wallProvider.createWalls(tileMap.map)
        // creating the snake
        val snake = Snake(
            setOf(
                SnakePiece(PointInt(screenWidth/2, screenHeight/2),true),
                SnakePiece(PointInt(screenWidth/2 + 1,screenHeight/2)),
                SnakePiece(PointInt(screenWidth/2 + 2,screenHeight/2))
            )
        )
        snake.render(tileMap.map)
	}

}

