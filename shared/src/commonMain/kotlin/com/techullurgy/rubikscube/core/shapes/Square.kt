package com.techullurgy.rubikscube.core.shapes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.lerp
import com.techullurgy.gameengine.Camera
import com.techullurgy.gameengine.SceneRenderer
import com.techullurgy.gameengine.Shader
import com.techullurgy.gameengine.Transformation
import com.techullurgy.gameengine.getBasicShaderWithMvp
import com.techullurgy.gameengine.getInstance

class Square(
    private val color: Color
) {
    companion object {
        private val shader: Shader by lazy { Shader.getBasicShaderWithMvp() }
        private const val COORDS_PER_VERTEX = 3

        // One Vertex Pass Length
        // (3 Pos + 4 Colors) = (3*4 + 4*4)
        private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

        private val indices = shortArrayOf(0, 1, 3, 2)
    }

    fun draw() {
        val mvp = Camera.getInstance().getCameraMatrix() * Transformation.calculate()
        shader.bind()

        shader.enableAttributeLocation(
            "vPosition",
            getCoordinates(),
            COORDS_PER_VERTEX,
            vertexStride
        )
        shader.setUniform4f("vColor", color.red, color.green, color.blue, color.alpha)
        shader.setUniformMatrix4fv("uMVPMatrix", mvp)
        SceneRenderer.drawTriangleStrips(indices)

        listOf(
            getLeftBorderCoordinates(),
            getTopBorderCoordinates(),
            getRightBorderCoordinates(),
            getBottomBorderCoordinates()
        ).forEach {
            val borderColor = Color.Black

            shader.enableAttributeLocation(
                "vPosition",
                it,
                COORDS_PER_VERTEX,
                vertexStride
            )
            shader.setUniform4f(
                "vColor",
                borderColor.red,
                borderColor.green,
                borderColor.blue,
                borderColor.alpha
            )
            shader.setUniformMatrix4fv("uMVPMatrix", mvp)
            SceneRenderer.drawTriangleStrips(indices)
        }

        shader.unbind()
    }
}

private fun getPercent(fraction: Float) = lerp(-.5f, .5f, fraction)

private fun getSquareCoordinatesFor(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float
): FloatArray {
    return floatArrayOf(
        left, top, 0f,
        right, top, 0f,
        right, bottom, 0f,
        left, bottom, 0f
    )
}

private fun Square.Companion.getCoordinates(): FloatArray {
    return getSquareCoordinatesFor(
        getPercent(.1f), getPercent(.1f), getPercent(.9f), getPercent(.9f)
    )
}

private fun Square.Companion.getLeftBorderCoordinates(): FloatArray {
    return getSquareCoordinatesFor(
        getPercent(0f),
        getPercent(0f),
        getPercent(.1f),
        getPercent(1f)
    )
}

private fun Square.Companion.getTopBorderCoordinates(): FloatArray {
    return getSquareCoordinatesFor(
        getPercent(0f),
        getPercent(0f),
        getPercent(1f),
        getPercent(.1f)
    )
}

private fun Square.Companion.getRightBorderCoordinates(): FloatArray {
    return getSquareCoordinatesFor(
        getPercent(.9f),
        getPercent(0f),
        getPercent(1f),
        getPercent(1f)
    )
}

private fun Square.Companion.getBottomBorderCoordinates(): FloatArray {
    return getSquareCoordinatesFor(
        getPercent(0f),
        getPercent(.9f),
        getPercent(1f),
        getPercent(1f)
    )
}