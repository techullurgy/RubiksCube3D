package com.techullurgy.rubikscube.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.MeshGradientPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.techullurgy.rubikscube.presentation.game.scene.GameScene

@Composable
@Preview
fun GameScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(MeshGradientPaint, contentScale = ContentScale.Crop)
    ) {
        GameScene(Modifier.fillMaxSize())
    }
}

private val MeshGradientPaint = MeshGradientPainter(
    rows = 1,
    columns = 2,
    hasBicubicColor = false
) {
    setVertex(0, 0, Offset(0f, 0f), Color(0xFF390040))
    setVertex(0, 1, Offset(1f, 0f), Color(0xFF009FB7))
    setVertex(0, 2, Offset(1f, 0.6f), Color(0xFF390040))
    setVertex(1, 0, Offset(0f, 1f), Color(0xFFBB4430))
    setVertex(1, 1, Offset(.6f, 1f), Color(0xFFBB4430))
    setVertex(1, 2, Offset(1f, 1f), Color(0xFF390040))
}