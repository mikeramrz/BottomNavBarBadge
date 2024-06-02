package com.example.bottomnavbarbadge.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.BarBadgeNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BarBadgeNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = BarBadgeNavigationDefaults.navigationContentColor(),
            selectedTextColor = BarBadgeNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = BarBadgeNavigationDefaults.navigationContentColor(),
            indicatorColor = BarBadgeNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

@Composable
fun BarBadgeNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = BarBadgeNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content,
        containerColor = Color.White,
    )
}

object BarBadgeNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}
