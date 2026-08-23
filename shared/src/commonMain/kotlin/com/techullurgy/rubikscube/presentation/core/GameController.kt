package com.techullurgy.rubikscube.presentation.core

import androidx.compose.runtime.Composable
import com.techullurgy.rubikscube.core.game.RubiksCubeGame
import com.techullurgy.rubikscube.core.models.GameMove

interface GameController {
    fun rotateCameraX(value: Float)
    fun rotateCameraY(value: Float)
    fun rotateCameraZ(value: Float)
    fun turn(move: GameMove)
    fun animateTurn(move: GameMove, value: Float)
}

@Composable
expect fun rememberGameController(game: RubiksCubeGame): GameController