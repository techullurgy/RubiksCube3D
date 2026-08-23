package com.techullurgy.gameengine

@Volatile
private var BASIC_SHADER_WITH_MVP: Shader? = null

actual fun Shader.Companion.getBasicShaderWithMvp(): Shader {
    if (BASIC_SHADER_WITH_MVP == null) {
        synchronized(this) {
            if (BASIC_SHADER_WITH_MVP == null) {
                BASIC_SHADER_WITH_MVP = Shader(
                    ShaderProgramSource(
                        BASIC_VERTEX_SHADER_WITH_MVP,
                        BASIC_FRAGMENT_SHADER
                    )
                )
            }
        }
    }

    return BASIC_SHADER_WITH_MVP!!
}