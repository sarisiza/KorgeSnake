package snake

import korlibs.math.geom.*

enum class SnakeDirection(
    val delta: Vector2I
) {
    UP(Vector2I(0,-1)),
    DOWN(Vector2I(0,+1)),
    LEFT(Vector2I(-1,0)),
    RIGHT(Vector2I(+1,0))
}
