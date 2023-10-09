package com.programmerofpersia.trends.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import com.programmerofpersia.trends.common.ui.theme.spacing
import com.programmerofpersia.trends.common.ui.theme.trTitleLarge

sealed class TrTopAppBarActions {
    object EndIconClicked : TrTopAppBarActions()

    object StartIconClicked : TrTopAppBarActions()
}

data class TrTopAppBarState(
    val isVisible: Boolean = true,
    val title: String?,
    val startIcon: ImageVector? = null,
    val endIcon: ImageVector? = null
) {
    companion object {
        val Hidden = TrTopAppBarState(isVisible = false, title = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrTopAppBar(
    state: TrTopAppBarState,
    onEndIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = {
            Text(
                text = state.title ?: "",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.trTitleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        actions = {
            if (state.endIcon != null) {
                Icon(
                    imageVector = state.endIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onEndIconClick()
                        }
                        .padding(
                            horizontal = MaterialTheme.spacing.grid_2,
                            vertical = MaterialTheme.spacing.grid_1
                        ),
                    tint = Color.DarkGray
                )
            }
        },
        scrollBehavior = scrollBehavior
    )

}