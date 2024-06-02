package com.example.bottomnavbarbadge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.bottomnavbarbadge.navigation.DASHBOARD_ROUTE
import com.example.bottomnavbarbadge.navigation.dashboardScreen
import com.example.bottomnavbarbadge.navigation.friendsScreen
import com.example.bottomnavbarbadge.navigation.settingsScreen

@Composable
fun BarBadgeNavHost(
    appState: BarBadgeAppState,
    modifier: Modifier = Modifier,
    startDestination: String = DASHBOARD_ROUTE
) {

    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        dashboardScreen()
        friendsScreen()
        settingsScreen()
    }

}