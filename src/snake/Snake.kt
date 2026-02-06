package snake

import korlibs.image.tiles.*

data class Snake(
    val pieces: Set<SnakePiece>
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
            map[piece.position.x,piece.position.y] =
                when {
                    piece.isHead -> {
                        // head of the snake
                        Tile(0)
                    }
                    piece == pieces.last() -> {
                        // tail of the snake
                        // we are assuming the list is sorted
                        Tile(1)
                    }
                    else -> {
                        Tile(2)
                    }
                }
        }
    }

    fun isTurning(previous: SnakePiece, following: SnakePiece): SnakeDirection? {

    }

}
