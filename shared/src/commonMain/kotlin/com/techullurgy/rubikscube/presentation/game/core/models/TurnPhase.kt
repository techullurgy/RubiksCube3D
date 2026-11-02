package com.techullurgy.rubikscube.presentation.game.core.models

internal enum class TurnPhase(val targetAngle: Float) {
    Idle(0f), Clockwise(90f), AntiClockwise(-90f)
}
