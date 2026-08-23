package com.techullurgy.gameengine

actual class OpenGL {
    actual companion object {
        actual fun glUseProgram(program: Int) {
        }

        actual fun glCreateShader(type: ShaderType): Int {
            TODO("Not yet implemented")
        }

        actual fun glShaderSource(shader: Int, code: String) {
        }

        actual fun glCompileShader(shader: Int) {
        }

        actual fun glCreateProgram(): Int {
            TODO("Not yet implemented")
        }

        actual fun glAttachShader(program: Int, shader: Int) {
        }

        actual fun glLinkProgram(program: Int) {
        }

        actual fun glGetAttribLocation(program: Int, name: String): Int {
            TODO("Not yet implemented")
        }

        actual fun glEnableVertexAttribArray(location: Int) {
        }

        actual fun glDisableVertexAttribArray(location: Int) {
        }

        actual fun glVertexAttribPointer(
            location: Int,
            size: Int,
            type: DataType,
            normalized: Boolean,
            stride: Int,
            values: Array<Number>
        ) {
        }

        actual fun glUniform4fv(
            location: Int,
            count: Int,
            values: FloatArray,
            offset: Int
        ) {
        }

        actual fun glUniform4f(
            location: Int,
            x: Float,
            y: Float,
            z: Float,
            w: Float
        ) {
        }

        actual fun glUniformMatrix4fv(
            location: Int,
            count: Int,
            transpose: Boolean,
            value: FloatArray,
            offset: Int
        ) {
        }

        actual fun glGetUniformLocation(program: Int, name: String): Int {
            TODO("Not yet implemented")
        }

        actual fun glViewport(x: Int, y: Int, width: Int, height: Int) {}

        actual fun glEnable(feature: Feature) {}

        actual fun glClear(bufferBits: List<BufferBit>) {}

        actual fun glClearColor(red: Float, green: Float, blue: Float, alpha: Float) {}
        actual fun glDrawElements(
            drawType: DrawType,
            size: Int,
            dataType: DataType,
            values: Array<Number>
        ) {
        }
    }
}