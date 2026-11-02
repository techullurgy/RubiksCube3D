package com.techullurgy.rubikscube.presentation.game.core.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.techullurgy.rubikscube.presentation.game.utils.setParent
import com.techullurgy.rubikscube.presentation.game.utils.transformMatrix
import io.github.erkko68.filament.compose.FilamentSceneScope
import io.github.erkko68.filament.compose.LocalFilamentEngine
import io.github.erkko68.filament.compose.scene.Position
import io.github.erkko68.filament.compose.scene.Rotation
import io.github.erkko68.filament.compose.scene.Scale

@Composable
internal fun FilamentSceneScope.Group(
    position: Position = Position(0f),
    rotation: Rotation = Rotation.Identity,
    scale: Scale = Scale(1f),
    pivot: Position = Position(0f),
    content: @Composable FilamentSceneScope.() -> Unit,
) {
    val engine = LocalFilamentEngine.current!!
    val outerParent = LocalParentEntity.current

    val groupEntity = remember(engine) { engine.entityManager.create() }

    DisposableEffect(groupEntity) {
        val tm = engine.transformManager
        tm.create(groupEntity)
        onDispose { tm.destroy(groupEntity) }
    }

    DisposableEffect(groupEntity, position, rotation, scale, pivot) {
        val tm = engine.transformManager
        tm.setTransform(tm.getInstance(groupEntity),
            transformMatrix(position, rotation, scale, pivot)
        )
        onDispose {}
    }

    // Nested groups: this group is itself a child of the outer one.
    DisposableEffect(groupEntity, outerParent) {
        engine.setParent(groupEntity, outerParent)
        onDispose {}
    }

    CompositionLocalProvider(
        LocalParentEntity provides groupEntity
    ) {
        content()
    }
}