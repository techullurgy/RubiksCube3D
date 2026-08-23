package com.techullurgy.gameengine

import dev.romainguy.kotlin.math.Float4
import dev.romainguy.kotlin.math.Mat4

class Shader internal constructor(
    source: ShaderProgramSource
) {
    private var programId = createShader(source)
    private val uniformLocationMap = mutableMapOf<String, Int>()
    private val attributesLocationMap = mutableMapOf<String, Int>()

    fun bind() {
        OpenGL.glUseProgram(programId)
    }

    fun unbind() {
        OpenGL.glUseProgram(0)
    }

    fun enableAttributeLocation(name: String, values: FloatArray, perSize: Int, stride: Int) {
        if (values.isEmpty()) {
            throw IllegalArgumentException("addBuffer values cannot be empty")
        }

        val location = if (attributesLocationMap.containsKey(name)) {
            attributesLocationMap[name]!!
        } else {
            val loc = OpenGL.glGetAttribLocation(programId, name)
            attributesLocationMap[name] = loc
            loc
        }

        OpenGL.glEnableVertexAttribArray(location)
        OpenGL.glVertexAttribPointer(
            location,
            perSize,
            DataType.FLOAT,
            false,
            stride,
            values.map { it as Number }.toTypedArray()
        )
    }

    fun disableAttributeLocation(name: String) {
        if (!attributesLocationMap.containsKey(name)) {
            throw IllegalStateException("No Attribute Location found for $name")
        }

        val loc = attributesLocationMap[name]!!
        OpenGL.glDisableVertexAttribArray(loc)
    }

    fun setUniform4fv(name: String, value: Float4) {
        val location = getUniformLocation(name)
        OpenGL.glUniform4fv(location, 1, value.toFloatArray(), 0)
    }

    fun setUniform4f(name: String, value1: Float, value2: Float, value3: Float, value4: Float) {
        val location = getUniformLocation(name)
        OpenGL.glUniform4f(location, value1, value2, value3, value4)
    }

    fun setUniformMatrix4fv(name: String, matrix: Mat4) {
        val location = getUniformLocation(name)
        OpenGL.glUniformMatrix4fv(location, 1, true, matrix.toFloatArray(), 0)
    }

    private fun createShader(source: ShaderProgramSource): Int {
        val vertexShader = loadShader(ShaderType.VERTEX_SHADER, source.vertex)
        val fragmentShader = loadShader(ShaderType.FRAGMENT_SHADER, source.fragment)

        return OpenGL.glCreateProgram().also {
            OpenGL.glAttachShader(it, vertexShader)
            OpenGL.glAttachShader(it, fragmentShader)
            OpenGL.glLinkProgram(it)
        }
    }

    private fun loadShader(type: ShaderType, shaderCode: String): Int {
        return OpenGL.glCreateShader(type).also { shader ->
            OpenGL.glShaderSource(shader, shaderCode)
            OpenGL.glCompileShader(shader)
        }
    }

    private fun getUniformLocation(name: String): Int {
        if (uniformLocationMap.containsKey(name)) {
            return uniformLocationMap[name]!!
        }

        val location = OpenGL.glGetUniformLocation(programId, name)
        if (location == -1) {
            println("Warning: uniform $name doesn't exists!!!")
        }

        uniformLocationMap[name] = location

        return location
    }

    companion object {}
}

expect fun Shader.Companion.getBasicShaderWithMvp(): Shader