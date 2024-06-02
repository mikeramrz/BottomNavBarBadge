package com.example.bottomnavbarbadge.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bottomnavbarbadge.BarBadgeIcons
import com.example.bottomnavbarbadge.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    DASHBOARD(
        selectedIcon = BarBadgeIcons.Dashboard,
        unselectedIcon = BarBadgeIcons.DashboardInactive,
        iconTextId = R.string.feature_title_dashboard,
        titleTextId = R.string.feature_title_dashboard
    ),
    FRIENDS(
        selectedIcon = BarBadgeIcons.Friends,
        unselectedIcon = BarBadgeIcons.FriendsInactive,
        iconTextId = R.string.feature_title_friends,
        titleTextId = R.string.feature_title_friends,
    ),
    SETTINGS(
        selectedIcon = BarBadgeIcons.Settings,
        unselectedIcon = BarBadgeIcons.SettingsInactive,
        iconTextId = R.string.feature_title_settings,
        titleTextId = R.string.feature_title_settings,
    ),
}

data class UnreadDestination(
    val destination: TopLevelDestination,
    val count: Int,
)
