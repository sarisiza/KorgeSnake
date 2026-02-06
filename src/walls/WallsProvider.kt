package walls

import korlibs.image.tiles.*

interface WallsProvider {
    fun createWalls(tileMapData: TileMapData)
}
