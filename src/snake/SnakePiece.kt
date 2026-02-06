package snake

import korlibs.math.geom.*

data class SnakePiece(
    val position: PointInt,
    val isHead: Boolean = false,
)
