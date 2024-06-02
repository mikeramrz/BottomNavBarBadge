package com.example.bottomnavbarbadge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.bottomnavbarbadge.DashboardRoute
import com.example.bottomnavbarbadge.SettingsRoute

const val DASHBOARD_ROUTE = "dashboard_route"


fun NavController.navigateToDashboard(navOptions: NavOptions) =
    navigate(DASHBOARD_ROUTE, navOptions)

fun NavGraphBuilder.dashboardScreen(
) {
    composable(
        route = DASHBOARD_ROUTE,
    ) {
        DashboardRoute()
    }
}
