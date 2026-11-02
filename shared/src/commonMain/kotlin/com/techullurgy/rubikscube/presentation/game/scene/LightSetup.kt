package com.techullurgy.rubikscube.presentation.game.scene

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.erkko68.filament.compose.FilamentSceneScope
import io.github.erkko68.filament.compose.scene.IndirectLight
import io.github.erkko68.filament.compose.scene.SphericalHarmonics
import kotlin.math.PI
import kotlin.math.sqrt

@Composable
internal fun FilamentSceneScope.LightSetup() {
    IndirectLight(
        irradiance = SphericalHarmonics.SolidRed,
        intensity = 60_000f
    )
}

private val SphericalHarmonics.Companion.SolidRed get() = SphericalHarmonics(
    bands = 3,
    coefficients = floatArrayOf(
        solidRedColor.red * COEFFICIENT, solidRedColor.green * COEFFICIENT, solidRedColor.blue * COEFFICIENT,
        0f, 0f, 0f,
        0f, 0f, 0f,
        0f, 0f, 0f,
        0f, 0f, 0f,
        0f, 0f, 0f,
        0f, 0f, 0f,
        0f, 0f, 0f,
        0f, 0f, 0f,
    )
)

private val COEFFICIENT = (1f/(2f*sqrt(PI)).toFloat())
private val solidRedColor = Color.Red