package snake

import korlibs.image.tiles.*
import korlibs.math.geom.slice.*

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
        pieces.forEachIndexed { index, piece ->
            val previous = if (index > 0) pieces[index-1] else null
            val following = if (index < pieces.lastIndex) pieces[index+1] else null
            val bodyOrientation = piece.isTurning(previous, following)
            map[piece.position.x,piece.position.y] =
                when {
                    piece.isHead -> {
                        // head of the snake
                        if (following == null) {
                            Tile(0) // no new pieces
                        } else {
                            val orientation = when (bodyOrientation) {
                                BodyOrientation.HORIZONTAL_LEFT -> SliceOrientation.MIRROR_HORIZONTAL
                                BodyOrientation.VERTICAL_UP -> SliceOrientation.ROTATE_90
                                BodyOrientation.VERTICAL_DOWN -> SliceOrientation.ROTATE_270
                                else -> SliceOrientation.NORMAL
                            }
                            Tile(4, orientation)
                        }
                    }
                    piece == pieces.last() -> {
                        // tail of the snake
                        // we are assuming the list is sorted
                        val orientation = when (bodyOrientation) {
                            BodyOrientation.HORIZONTAL_LEFT -> SliceOrientation.MIRROR_HORIZONTAL
                            BodyOrientation.VERTICAL_UP -> SliceOrientation.ROTATE_270
                            BodyOrientation.VERTICAL_DOWN -> SliceOrientation.ROTATE_90
                            else -> SliceOrientation.NORMAL
                        }
                        Tile(1, orientation)
                    }
                    else -> {
                        when (bodyOrientation) {
                            BodyOrientation.HORIZONTAL_LEFT -> Tile(2)
                            BodyOrientation.HORIZONTAL_RIGHT -> Tile(2)
                            BodyOrientation.UP_LEFT -> Tile(3, SliceOrientation.MIRROR_VERTICAL)
                            BodyOrientation.UP_RIGHT -> Tile(3, SliceOrientation.ROTATE_180)
                            BodyOrientation.DOWN_LEFT -> Tile(3, SliceOrientation.NORMAL) // original
                            BodyOrientation.DOWN_RIGHT -> Tile(3, SliceOrientation.MIRROR_HORIZONTAL)
                            else -> Tile(2, SliceOrientation.ROTATE_90)
                        }
                    }
                }
        }
    }

    fun SnakePiece.isTurning(previous: SnakePiece?, following: SnakePiece?): BodyOrientation {
        val previousPoint = previous?.position
        val followingPoint = following?.position
        val currentPoint = this.position
        return when {
            // head
            previousPoint == null -> this.orientation
            // tail
            followingPoint == null -> {
                when {
                    previousPoint.x > currentPoint.x && previousPoint.y < currentPoint.y && previous.orientation == BodyOrientation.UP_RIGHT -> BodyOrientation.VERTICAL_DOWN
                    previousPoint.x < currentPoint.x && previousPoint.y < currentPoint.y && previous.orientation == BodyOrientation.UP_LEFT -> BodyOrientation.VERTICAL_DOWN
                    previousPoint.x == currentPoint.x && previousPoint.y < currentPoint.y -> BodyOrientation.VERTICAL_DOWN

                    previousPoint.x > currentPoint.x && previousPoint.y < currentPoint.y && previous.orientation == BodyOrientation.DOWN_LEFT -> BodyOrientation.HORIZONTAL_RIGHT
                    previousPoint.x > currentPoint.x && previousPoint.y > currentPoint.y && previous.orientation == BodyOrientation.UP_LEFT -> BodyOrientation.HORIZONTAL_RIGHT
                    previousPoint.y == currentPoint.y && previousPoint.x > currentPoint.x -> BodyOrientation.HORIZONTAL_RIGHT

                    previousPoint.x > currentPoint.x && previousPoint.y < currentPoint.y && previous.orientation == BodyOrientation.DOWN_RIGHT -> BodyOrientation.VERTICAL_UP
                    previousPoint.x < currentPoint.x && previousPoint.y < currentPoint.y && previous.orientation == BodyOrientation.DOWN_LEFT -> BodyOrientation.VERTICAL_UP
                    previousPoint.x == currentPoint.x && previousPoint.y > currentPoint.y -> BodyOrientation.VERTICAL_UP

                    else -> BodyOrientation.HORIZONTAL_LEFT
                }
            }
            previousPoint.x == followingPoint.x -> BodyOrientation.VERTICAL_UP
            previousPoint.y == followingPoint.y -> BodyOrientation.HORIZONTAL_LEFT
            previousPoint.x > followingPoint.x && previousPoint.y > followingPoint.y -> BodyOrientation.DOWN_RIGHT
            previousPoint.x > followingPoint.x && previousPoint.y < followingPoint.y -> BodyOrientation.UP_RIGHT
            previousPoint.x < followingPoint.x && previousPoint.y > followingPoint.y -> BodyOrientation.DOWN_LEFT
            previousPoint.x < followingPoint.x && previousPoint.y < followingPoint.y -> BodyOrientation.UP_LEFT
            else -> BodyOrientation.HORIZONTAL_LEFT
        }
    }

}
