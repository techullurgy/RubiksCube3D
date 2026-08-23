package com.techullurgy.rubikscube.core.renderer

import com.techullurgy.rubikscube.core.models.Move

interface RubiksCubeGameRenderer {
    fun rotateCameraX(angle: Float)
    fun rotateCameraY(angle: Float)
    fun rotateCameraZ(angle: Float)

    fun turnX(index: Int, dir: Int)
    fun turnY(index: Int, dir: Int)
    fun turnZ(index: Int, dir: Int)

    fun updateCurrentMove(move: Move, angle: Float)

    fun onSurfaceCreated()
    fun onSurfaceChanged(width: Int, height: Int)
    fun onDrawFrame()
}