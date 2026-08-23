package com.techullurgy.gameengine

internal val BASIC_VERTEX_SHADER_WITH_MVP = """
    uniform mat4 uMVPMatrix;
    attribute vec4 vPosition;
    void main() {
        gl_Position = uMVPMatrix * vPosition;
    }
""".trimIndent()

internal val BASIC_FRAGMENT_SHADER = """
    precision mediump float;
    uniform vec4 vColor;
    void main() {
        gl_FragColor = vColor;
    }
""".trimIndent()