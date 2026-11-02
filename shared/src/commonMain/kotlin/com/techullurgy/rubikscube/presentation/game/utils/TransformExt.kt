package com.techullurgy.rubikscube.presentation.game.utils

import io.github.erkko68.filament.Engine
import io.github.erkko68.filament.Entity
import io.github.erkko68.filament.compose.scene.Position
import io.github.erkko68.filament.compose.scene.Rotation
import io.github.erkko68.filament.compose.scene.Scale
import io.github.erkko68.filament.utils.Float2
import io.github.erkko68.filament.utils.Float3
import io.github.erkko68.filament.utils.Mat3
import io.github.erkko68.filament.utils.radians
import kotlin.math.cos
import kotlin.math.sin

internal fun transformMatrix(
    position: Position,
    rotation: Rotation,
    scale: Scale,
    pivot: Position = Position(0f),
): FloatArray {
    val x = rotation.x; val y = rotation.y; val z = rotation.z; val w = rotation.w

    // Pre-compute the (R·S) basis columns once — used twice (for the matrix entries and to
    // shift the translation column so the pivot ends up where `position` points).
    val m00 = (1f - 2f * (y * y + z * z)) * scale.x
    val m01 = (2f * (x * y + w * z)) * scale.x
    val m02 = (2f * (x * z - w * y)) * scale.x

    val m10 = (2f * (x * y - w * z)) * scale.y
    val m11 = (1f - 2f * (x * x + z * z)) * scale.y
    val m12 = (2f * (y * z + w * x)) * scale.y

    val m20 = (2f * (x * z + w * y)) * scale.z
    val m21 = (2f * (y * z - w * x)) * scale.z
    val m22 = (1f - 2f * (x * x + y * y)) * scale.z

    // Translation column: position − (R·S)·pivot. Cancels exactly to `position` when pivot=0.
    val tx = position.x - (m00 * pivot.x + m10 * pivot.y + m20 * pivot.z)
    val ty = position.y - (m01 * pivot.x + m11 * pivot.y + m21 * pivot.z)
    val tz = position.z - (m02 * pivot.x + m12 * pivot.y + m22 * pivot.z)

    return floatArrayOf(
        m00, m01, m02, 0f,   // column 0
        m10, m11, m12, 0f,   // column 1
        m20, m21, m22, 0f,   // column 2
        tx,  ty,  tz,  1f,   // column 3 (translation, pivot-adjusted)
    )
}

internal fun Engine.setParent(entity: Entity, parent: Entity?) {
    val tm = transformManager
    if (!tm.hasComponent(entity)) {
        if (parent == null) return
        tm.create(entity)
    }
    tm.setParent(tm.getInstance(entity), if (parent != null) tm.getInstance(parent) else 0)
}

internal fun Float3.rotateX(angle: Float): Float3 {
    val c = cos(radians(angle))
    val s = sin(radians(angle))

    return Mat3.of(
        1f, 0f, 0f,
        0f, c, -s,
        0f, s, c
    ) * this
}

internal fun Float3.rotateY(angle: Float): Float3 {
    val c = cos(radians(angle))
    val s = sin(radians(angle))

    return Mat3.of(
        c, 0f, s,
        0f, 1f, 0f,
        -s, 0f, c
    ) * this
}

internal fun Float3.rotateZ(angle: Float): Float3 {
    val c = cos(radians(angle))
    val s = sin(radians(angle))

    return Mat3.of(
        c, -s, 0f,
        s, c, 0f,
        0f, 0f, 1f
    ) * this
}

internal fun rotate2D(angle: Float, position: Float2): Float2
    = Float3(position, 0f).rotateZ(angle).xy