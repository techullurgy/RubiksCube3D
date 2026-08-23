package com.techullurgy.rubikscube.presentation.views

import android.opengl.GLSurfaceView
import com.techullurgy.rubikscube.core.renderer.RubiksCubeGameRenderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

internal class RubiksCubeGameViewRenderer(
    coreRenderer: RubiksCubeGameRenderer
): GLSurfaceView.Renderer, RubiksCubeGameRenderer by coreRenderer {

    override fun onDrawFrame(gl: GL10?) = onDrawFrame()

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) = onSurfaceChanged(width, height)

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) = onSurfaceCreated()
}