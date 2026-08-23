package com.techullurgy.gameengine

expect class OpenGL {
    companion object {
        fun glUseProgram(program: Int)

        fun glCreateShader(type: ShaderType): Int

        fun glShaderSource(shader: Int, code: String)

        fun glCompileShader(shader: Int)

        fun glCreateProgram(): Int

        fun glAttachShader(program: Int, shader: Int)

        fun glLinkProgram(program: Int)

        fun glGetAttribLocation(program: Int, name: String): Int

        fun glEnableVertexAttribArray(location: Int)

        fun glDisableVertexAttribArray(location: Int)

        fun glVertexAttribPointer(location: Int, size: Int, type: DataType, normalized: Boolean, stride: Int, values: Array<Number>)

        fun glUniform4fv(location: Int, count: Int, values: FloatArray, offset: Int)

        fun glUniform4f(location: Int, x: Float, y: Float, z: Float, w: Float)

        fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatArray, offset: Int)

        fun glGetUniformLocation(program: Int, name: String): Int

        fun glViewport(x: Int, y: Int, width: Int, height: Int)

        fun glEnable(feature: Feature)

        fun glClear(bufferBits: List<BufferBit>)

        fun glClearColor(red: Float, green: Float, blue: Float, alpha: Float)

        fun glDrawElements(drawType: DrawType, size: Int, dataType: DataType, values: Array<Number>)
    }
}

enum class ShaderType {
    VERTEX_SHADER, FRAGMENT_SHADER
}

enum class DataType {
    FLOAT, UNSIGNED_SHORT
}

enum class DrawType {
    TRIANGLE_STRIPS
}

enum class Feature {
    DEPTH_TEST
}

enum class BufferBit {
    COLOR, DEPTH
}