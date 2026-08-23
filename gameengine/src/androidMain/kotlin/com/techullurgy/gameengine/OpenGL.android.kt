package com.techullurgy.gameengine

import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder

private typealias GLES = GLES30

actual class OpenGL {
    actual companion object {
        actual fun glUseProgram(program: Int) {
            GLES.glUseProgram(program)
        }

        actual fun glCreateShader(type: ShaderType): Int {
            return GLES.glCreateShader(
                when(type) {
                    ShaderType.VERTEX_SHADER -> GLES.GL_VERTEX_SHADER
                    ShaderType.FRAGMENT_SHADER -> GLES.GL_FRAGMENT_SHADER
                }
            )
        }

        actual fun glShaderSource(shader: Int, code: String) {
            GLES.glShaderSource(shader, code)
        }

        actual fun glCompileShader(shader: Int) {
            GLES.glCompileShader(shader)
        }

        actual fun glCreateProgram(): Int {
            return GLES.glCreateProgram()
        }

        actual fun glAttachShader(program: Int, shader: Int) {
            GLES.glAttachShader(program, shader)
        }

        actual fun glLinkProgram(program: Int) {
            GLES.glLinkProgram(program)
        }

        actual fun glGetAttribLocation(program: Int, name: String): Int {
            return GLES.glGetAttribLocation(program, name)
        }

        actual fun glEnableVertexAttribArray(location: Int) {
            GLES.glEnableVertexAttribArray(location)
        }

        actual fun glDisableVertexAttribArray(location: Int) {
            GLES.glDisableVertexAttribArray(location)
        }

        actual fun glVertexAttribPointer(
            location: Int,
            size: Int,
            type: DataType,
            normalized: Boolean,
            stride: Int,
            values: Array<Number>
        ) {
            when(type) {
                DataType.FLOAT -> {
                    val floatArray: FloatArray = values.map { it as Float }.toFloatArray()
                    val vertexBuffer = ByteBuffer.allocateDirect(floatArray.size * 4).apply {
                        order(ByteOrder.nativeOrder())
                        asFloatBuffer().apply {
                            put(floatArray)
                            position(0)
                        }
                    }
                    GLES.glVertexAttribPointer(location, size, GLES.GL_FLOAT, normalized, stride, vertexBuffer)
                }
                DataType.UNSIGNED_SHORT -> {
                    val shortArray: ShortArray = values.map { it as Short }.toShortArray()
                    val vertexBuffer = ByteBuffer.allocateDirect(shortArray.size * 2).apply {
                        order(ByteOrder.nativeOrder())
                        asShortBuffer().apply {
                            put(shortArray)
                            position(0)
                        }
                    }
                    GLES.glVertexAttribPointer(location, size, GLES.GL_UNSIGNED_SHORT, normalized, stride, vertexBuffer)
                }
            }
        }

        actual fun glUniform4fv(
            location: Int,
            count: Int,
            values: FloatArray,
            offset: Int
        ) {
            GLES.glUniform4fv(location, count, values, offset)
        }

        actual fun glUniform4f(
            location: Int,
            x: Float,
            y: Float,
            z: Float,
            w: Float
        ) {
            GLES.glUniform4f(location, x, y, z, w)
        }

        actual fun glUniformMatrix4fv(
            location: Int,
            count: Int,
            transpose: Boolean,
            value: FloatArray,
            offset: Int
        ) {
            GLES.glUniformMatrix4fv(location, count, transpose, value, offset)
        }

        actual fun glGetUniformLocation(program: Int, name: String): Int {
            return GLES.glGetUniformLocation(program, name)
        }


        actual fun glViewport(x: Int, y: Int, width: Int, height: Int) {
            GLES.glViewport(x, y, width, height)
        }

        actual fun glEnable(feature: Feature) {
            when(feature) {
                Feature.DEPTH_TEST -> {
                    GLES.glEnable(GLES.GL_DEPTH_TEST)
                }
            }
        }

        actual fun glClear(bufferBits: List<BufferBit>) {
            val bits = bufferBits.fold(0) { acc, bit ->
                when(bit) {
                    BufferBit.COLOR -> acc or GLES.GL_COLOR_BUFFER_BIT
                    BufferBit.DEPTH -> acc or GLES.GL_DEPTH_BUFFER_BIT
                }
            }
            GLES.glClear(bits)
        }

        actual fun glClearColor(red: Float, green: Float, blue: Float, alpha: Float) {
            GLES.glClearColor(red, green, blue, alpha)
        }

        actual fun glDrawElements(
            drawType: DrawType,
            size: Int,
            dataType: DataType,
            values: Array<Number>
        ) {
            val glDrawType = when(drawType) {
                DrawType.TRIANGLE_STRIPS -> GLES.GL_TRIANGLE_STRIP
            }

            when(dataType) {
                DataType.FLOAT -> {
                    val floatArray: FloatArray = values.map { it as Float }.toFloatArray()
                    val indexBuffer = ByteBuffer.allocateDirect(floatArray.size * 4).apply {
                        order(ByteOrder.nativeOrder())
                        asFloatBuffer().apply {
                            put(floatArray)
                            position(0)
                        }
                    }
                    GLES.glDrawElements(glDrawType, size, GLES.GL_FLOAT, indexBuffer)
                }
                DataType.UNSIGNED_SHORT -> {
                    val shortArray: ShortArray = values.map { it as Short }.toShortArray()
                    val indexBuffer = ByteBuffer.allocateDirect(shortArray.size * 2).apply {
                        order(ByteOrder.nativeOrder())
                        asShortBuffer().apply {
                            put(shortArray)
                            position(0)
                        }
                    }
                    GLES.glDrawElements(glDrawType, size, GLES.GL_UNSIGNED_SHORT, indexBuffer)
                }
            }
        }
    }
}