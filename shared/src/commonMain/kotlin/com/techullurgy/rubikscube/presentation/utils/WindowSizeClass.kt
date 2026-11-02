package com.techullurgy.rubikscube.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalMediaQueryApi
import androidx.compose.ui.UiMediaScope
import androidx.compose.ui.derivedMediaQuery
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.computeWindowSizeClass

@OptIn(ExperimentalMediaQueryApi::class)
internal val UiMediaScope.windowSizeClass
    @Composable get() = derivedMediaQuery {
        WindowSizeClass.BREAKPOINTS_V2.computeWindowSizeClass(
            widthDp = windowWidth.value,
            heightDp = windowHeight.value
        )
    }.value