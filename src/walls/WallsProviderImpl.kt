package walls

import korlibs.image.tiles.*
import korlibs.math.geom.slice.*

class WallsProviderImpl : WallsProvider {
    override fun createWalls(tileMapData: TileMapData) {
        val screenWidth = tileMapData.width
        val screenHeight = tileMapData.height
        for (y in 0 until screenHeight) {
            for (x in 0 until screenWidth) {
                val wallOrientation = when {
                    x == 0  && y == 0 -> SliceOrientation.NORMAL.flippedX()
                    x == 0 && y == screenHeight - 1 -> SliceOrientation.NORMAL.flippedY().flippedX()
                    x == screenWidth - 1 && y == screenHeight - 1 -> SliceOrientation.NORMAL.flippedY()
                    y > 0 && y < screenHeight - 1 -> SliceOrientation.NORMAL.rotatedRight()
                    else -> SliceOrientation.NORMAL
                }
                tileMapData[x,y] = when {
                    (x == 0 || x == screenWidth - 1) && (y == 0 || y == screenHeight - 1) -> Tile(19, wallOrientation)
                    (x == 0 || x == screenWidth - 1) || (y == 0 || y == screenHeight - 1) -> Tile(18, wallOrientation)
                    else -> Tile.INVALID
                }
            }
        }
    }
}
