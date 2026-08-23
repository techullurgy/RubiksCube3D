package com.techullurgy.gameengine

object SceneRenderer {
    fun drawTriangleStrips(indices: ShortArray) {
        OpenGL.glDrawElements(
            drawType = DrawType.TRIANGLE_STRIPS,
            size = indices.size,
            dataType = DataType.UNSIGNED_SHORT,
            values = indices.map { it as Number }.toTypedArray()
        )
    }
}