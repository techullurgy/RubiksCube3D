package com.techullurgy.rubikscube.presentation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.techullurgy.rubikscube.core.game.RubiksCubeGame
import com.techullurgy.rubikscube.core.models.GameMove
import com.techullurgy.rubikscube.presentation.views.RubiksCubeGameView

class AndroidGameController(
    private val game: RubiksCubeGame
): GameController {
    private var view: RubiksCubeGameView? = null

    fun attach(view: RubiksCubeGameView) {
        this.view = view
    }

    fun detach() {
        view = null
    }

    override fun rotateCameraX(value: Float) {
        view?.rotateCameraX(value)
    }

    override fun rotateCameraY(value: Float) {
        view?.rotateCameraY(value)
    }

    override fun rotateCameraZ(value: Float) {
        view?.rotateCameraZ(value)
    }

    override fun turn(move: GameMove) {
        view?.turn(move)
        game.move(move)
    }

    override fun animateTurn(
        move: GameMove,
        value: Float
    ) {
        view?.animateTurn(move, value)
    }
}

@Composable
actual fun rememberGameController(game: RubiksCubeGame): GameController {
    return remember(game) { AndroidGameController(game) }
}