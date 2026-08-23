package com.techullurgy.rubikscube.presentation.views

import android.content.Context
import android.opengl.GLSurfaceView
import com.techullurgy.rubikscube.core.models.GameMove
import com.techullurgy.rubikscube.core.models.Move
import com.techullurgy.rubikscube.core.renderer.AbstractRubiksCubeGameRenderer
import kotlin.math.abs

class RubiksCubeGameView(
    context: Context
): GLSurfaceView(context) {
    private val renderer: RubiksCubeGameViewRenderer

    init {
        setEGLContextClientVersion(3)
        renderer = RubiksCubeGameViewRenderer(
            coreRenderer = object: AbstractRubiksCubeGameRenderer() {}
        )
        setRenderer(renderer)

        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun rotateCameraX(angle: Float) {
        renderer.rotateCameraX(angle)
        requestRender()
    }

    fun rotateCameraY(angle: Float) {
        renderer.rotateCameraY(angle)
        requestRender()
    }

    fun rotateCameraZ(angle: Float) {
        renderer.rotateCameraZ(angle)
        requestRender()
    }

    fun turn(gameMove: GameMove) {
        val move = Move.fromGameMove(gameMove)

        if (abs(move.x) > 0) {
            renderer.turnX(move.x, move.dir)
        } else if (abs(move.y) > 0) {
            renderer.turnY(move.y, move.dir)
        } else if (abs(move.z) > 0) {
            renderer.turnZ(move.z, move.dir)
        }
        requestRender()
    }

    fun animateTurn(gameMove: GameMove, angle: Float) {
        val move = Move.fromGameMove(gameMove)
        renderer.updateCurrentMove(move, angle)
        requestRender()
    }
}