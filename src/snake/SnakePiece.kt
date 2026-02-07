package snake

import korlibs.math.geom.*

data class SnakePiece(
    val position: PointInt,
    val orientation: BodyOrientation = BodyOrientation.HORIZONTAL_LEFT,
    val isHead: Boolean = false,
)
