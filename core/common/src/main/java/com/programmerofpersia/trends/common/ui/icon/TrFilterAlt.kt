package com.programmerofpersia.trends.common.ui.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Outlined.TrFilterAlt: ImageVector
    get() {
        if (_filterAlt != null) {
            return _filterAlt!!
        }
        _filterAlt = materialIcon(name = "Outlined.FilterAlt") {
            materialPath {
                moveTo(7.0f, 6.0f)
                horizontalLineToRelative(10.0f)
                lineToRelative(-5.01f, 6.3f)
                lineTo(7.0f, 6.0f)
                close()
                moveTo(4.25f, 5.61f)
                curveTo(6.27f, 8.2f, 10.0f, 13.0f, 10.0f, 13.0f)
                verticalLineToRelative(6.0f)
                curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(2.0f)
                curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                verticalLineToRelative(-6.0f)
                curveToRelative(0.0f, 0.0f, 3.72f, -4.8f, 5.74f, -7.39f)
                curveTo(20.25f, 4.95f, 19.78f, 4.0f, 18.95f, 4.0f)
                horizontalLineTo(5.04f)
                curveTo(4.21f, 4.0f, 3.74f, 4.95f, 4.25f, 5.61f)
                close()
            }
        }
        return _filterAlt!!
    }

private var _filterAlt: ImageVector? = null