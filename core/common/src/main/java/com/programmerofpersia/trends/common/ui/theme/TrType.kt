package com.programmerofpersia.trends.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp


val Typography.trTitleLarge: TextStyle
    @Composable
    get() = Typography().titleLarge

val Typography.trTitleMedium: TextStyle
    @Composable
    get() = Typography().titleMedium

val Typography.trTitleSmall: TextStyle
    @Composable
    get() = Typography().titleSmall

val Typography.trBodyXLarge: TextStyle
    @Composable
    get() = Typography().bodyLarge.copy(
        fontSize = 20.sp
    )

val Typography.trBodyLarge: TextStyle
    @Composable
    get() = Typography().bodyLarge

val Typography.trBodyMedium: TextStyle
    @Composable
    get() = Typography().bodyMedium

val Typography.trBodySmall: TextStyle
    @Composable
    get() = Typography().bodySmall

val Typography.trLabelXLarge: TextStyle
    @Composable
    get() = Typography().labelLarge.copy(
        fontSize = 16.sp
    )

val Typography.trLabelLarge: TextStyle
    @Composable
    get() = Typography().labelLarge

val Typography.trLabelMedium: TextStyle
    @Composable
    get() = Typography().labelMedium

val Typography.trLabelSmall: TextStyle
    @Composable
    get() = Typography().labelSmall