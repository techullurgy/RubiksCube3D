package com.techullurgy.rubikscube.presentation.game.core.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.techullurgy.rubikscube.presentation.game.utils.setParent
import com.techullurgy.rubikscube.presentation.game.utils.transformMatrix
import io.github.erkko68.filament.Box
import io.github.erkko68.filament.Entity
import io.github.erkko68.filament.IndexBuffer
import io.github.erkko68.filament.RenderableManager
import io.github.erkko68.filament.VertexBuffer
import io.github.erkko68.filament.compose.FilamentEffect
import io.github.erkko68.filament.compose.FilamentSceneScope
import io.github.erkko68.filament.compose.LocalFilamentEngine
import io.github.erkko68.filament.compose.scene.LinearColor
import io.github.erkko68.filament.compose.scene.Position
import io.github.erkko68.filament.compose.scene.Rotation
import io.github.erkko68.filament.compose.scene.Scale
import io.github.erkko68.filament.compose.scene.rememberMaterial
import io.github.erkko68.filament.compose.scene.rememberMaterialInstance
import io.github.erkko68.filament.toBytes
import io.github.erkko68.filament.utils.Float3
import io.github.erkko68.filament.utils.Quaternion
import rubikscube.shared.generated.resources.Res

@Composable
internal fun FilamentSceneScope.OneSidedPlane(
    color: LinearColor,
    position: Position = Position(0f),
    rotation: Rotation = Rotation(Quaternion.fromEuler(Float3(0f, 0f, 0f))),
    scale: Scale = Scale(1f),
    onFaceCreated: (Entity) -> Unit
) {
    val engine = LocalFilamentEngine.current!!
    val planeEntity = remember { engine.entityManager.create().also { onFaceCreated(it) } }

    val outerParentEntity = LocalParentEntity.current

    DisposableEffect(planeEntity, outerParentEntity) {
        engine.setParent(planeEntity, outerParentEntity)
        onDispose {  }
    }

    DisposableEffect(engine, planeEntity, position, rotation, scale) {
        // Apply spatial transformations (Rotation + Translation)
        val tm = engine.transformManager
        val instance = tm.getInstance(planeEntity)
        tm.setTransform(instance, transformMatrix(position, rotation, scale))
        onDispose {  }
    }

    val material = rememberMaterial(engine = engine, onError = { println(it) }) { getMaterialBytes() }

    val materialInstance = rememberMaterialInstance(material)?.apply {
        val black = LinearColor(0.02f, 0.02f, 0.02f)
        setParameter("faceColor", color.r, color.g, color.b)
        setParameter("borderColor", black.r, black.g, black.b)
        setParameter("borderThickness", 0.08f)
    }

    materialInstance?.let {
        FilamentEffect(engine, materialInstance) {
            // Vertices for a standard 1x1 plane centered on X-Y axis
            val vertexData = floatArrayOf(
                -0.5f, -0.5f,  0f, /*(uv)*/ 0f, 0f,  // 0: Bottom-Left
                0.5f, -0.5f,  0f,           1f, 0f,  // 1: Bottom-Right
                0.5f,  0.5f,  0f,           1f, 1f,  // 2: Top-Right
                -0.5f, 0.5f,  0f,           0f, 1f   // 3: Top-Left
            ).toBytes()

            // Index array to draw 2 triangles (Counter-Clockwise winding order)
            val indexData = shortArrayOf(
                0, 1, 2,  // First Triangle
                0, 2, 3   // Second Triangle
            ).toBytes()

            val vBuffer = VertexBuffer.Builder()
                .vertexCount(4)
                .bufferCount(1)
                .attribute(VertexBuffer.VertexAttribute.POSITION, 0, VertexBuffer.AttributeType.FLOAT3, 0, 20)
                .attribute(VertexBuffer.VertexAttribute.UV0, 0, VertexBuffer.AttributeType.FLOAT2, 12, 20)
                .build(engine)
            vBuffer.setBufferAt(engine, 0, vertexData)

            val iBuffer = IndexBuffer.Builder()
                .indexCount(6)
                .bufferType(IndexBuffer.IndexType.USHORT)
                .build(engine)
            iBuffer.setBuffer(engine, indexData)

            // Build the renderable mesh component
            RenderableManager.Builder(1)
                .geometry(0, RenderableManager.PrimitiveType.TRIANGLES, vBuffer, iBuffer, 0, 6)
                .material(0, materialInstance)
                .boundingBox(Box(floatArrayOf(0f, 0f, 0f), floatArrayOf(0.5f, 0.5f, 0.01f)))
                .build(engine, planeEntity)

            scene.addEntity(planeEntity)

            onDispose {
                scene.remove(planeEntity)
                engine.destroy(planeEntity)
                engine.renderableManager.destroy(planeEntity)
                engine.entityManager.destroy(planeEntity)
            }
        }
    }
}

suspend fun getMaterialBytes(): ByteArray {
    return Res.readBytes("files/materials/rubik_material.filamat")
}