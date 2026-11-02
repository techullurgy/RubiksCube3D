package com.techullurgy.rubikscube.presentation.game.core.models

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.spring

internal class CubieRotator {
    val rotatorTransition: TransitionState<TurnPhase> field = SeekableTransitionState(TurnPhase.Idle)

    val targetState get() = rotatorTransition.targetState
    val fraction get() = rotatorTransition.fraction

    suspend fun seekTo(fraction: Float, target: TurnPhase) {
        rotatorTransition.seekTo(fraction, target)
    }

    suspend fun animateTo(target: TurnPhase, spec: FiniteAnimationSpec<Float> = spring()) {
        rotatorTransition.animateTo(target, spec)
    }

    suspend fun snapTo(target: TurnPhase) {
        rotatorTransition.snapTo(target)
    }
}