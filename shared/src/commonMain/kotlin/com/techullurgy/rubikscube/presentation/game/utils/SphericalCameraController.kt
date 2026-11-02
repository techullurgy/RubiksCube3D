package com.techullurgy.rubikscube.presentation.game.utils

import io.github.erkko68.filament.compose.scene.CameraState
import io.github.erkko68.filament.compose.scene.Position
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal class SphericalCameraController(
    private val cameraState: CameraState,
    private val sensitivity: Float = 0.005f
) {
    // Read the fixed target
    private val target = cameraState.target

    // Distance R between initial eye and target
    private var radius: Float = 0.0f

    // Current spherical coordinates (radians)
    private var azimuth: Float = 0.0f    // Yaw around vertical axis
    private var elevation: Float = 0.0f  // Pitch relative to horizontal plane

    private val elevationLimit = (PI / 2.0 - 0.001).toFloat()
    private val twoPi = (2.0 * PI).toFloat()

    init {
        syncWithCameraState(cameraState)
    }

    /**
     * Orbit around the target.
     * Updates cameraState.eye which triggers automatic UI re-renders.
     */
    fun orbit(deltaX: Float, deltaY: Float) {
        azimuth -= deltaX * sensitivity
        elevation += deltaY * sensitivity

        // Clamp elevation to avoid gimbal lock / pole flipping
        elevation = elevation.coerceIn(-elevationLimit, elevationLimit)

        // Normalize azimuth into [0, 2π)
        azimuth %= twoPi
        if (azimuth < 0.0f) azimuth += twoPi

        // Calculate new Cartesian eye position on the sphere
        val cosElevation = cos(elevation)
        val newEyeX = target.x + radius * cosElevation * sin(azimuth)
        val newEyeY = target.y + radius * sin(elevation)
        val newEyeZ = target.z + radius * cosElevation * cos(azimuth)

        // Mutating cameraState.eye notifies observers and updates the view
        cameraState.eye = Position(newEyeX, newEyeY, newEyeZ)
    }

    fun updateCameraState(cameraState: CameraState) {
        syncWithCameraState(cameraState)
    }

    private fun syncWithCameraState(cameraState: CameraState) {
        val eye = cameraState.eye

        // Compute relative vector: Eye - Target
        val dx = (eye.x - target.x).toDouble()
        val dy = (eye.y - target.y).toDouble()
        val dz = (eye.z - target.z).toDouble()

        // Calculate initial radius
        radius = sqrt(dx * dx + dy * dy + dz * dz).toFloat()

        // Derive initial elevation (phi) and azimuth (theta)
        if (radius > 1e-6f) {
            elevation = asin((dy / radius).coerceIn(-1.0, 1.0)).toFloat()
            azimuth = atan2(dx, dz).toFloat()
        }
    }
}