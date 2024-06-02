package com.example.bottomnavbarbadge

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.bottomnavbarbadge.navigation.DASHBOARD_ROUTE
import com.example.bottomnavbarbadge.navigation.FRIENDS_ROUTE
import com.example.bottomnavbarbadge.navigation.SETTINGS_ROUTE
import com.example.bottomnavbarbadge.navigation.TopLevelDestination
import com.example.bottomnavbarbadge.navigation.navigateToDashboard
import com.example.bottomnavbarbadge.navigation.navigateToFriends
import com.example.bottomnavbarbadge.navigation.navigateToSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberBarBadgeAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): BarBadgeAppState {
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
    ) {
        BarBadgeAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            windowSizeClass = windowSizeClass,
        )
    }
}

@Stable
class BarBadgeAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            DASHBOARD_ROUTE -> TopLevelDestination.DASHBOARD
            FRIENDS_ROUTE -> TopLevelDestination.FRIENDS
            SETTINGS_ROUTE -> TopLevelDestination.SETTINGS
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    //Map of top level destinations, key is the route.
    val topLevelDestinations: List<TopLevelDestination> = buildList {
        add(TopLevelDestination.DASHBOARD)
        add(TopLevelDestination.FRIENDS)
        add(TopLevelDestination.SETTINGS)
    }

    //A real example would be our friend requests that we haven't seen.
    // But for simplicity we will just return some dummy data
    val topLevelDestinationUnseenFriendRequestCountFlow: StateFlow<Map<TopLevelDestination, Int>> =
        getUnseenIncomingFriendRequestsCount()
            .map { result ->
                if (result.isSuccess && result.getOrDefault(0) > 0) {
                    mapOf(TopLevelDestination.FRIENDS to result.getOrThrow())
                } else {
                    emptyMap()
                }
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyMap()
            )


    private fun getUnseenIncomingFriendRequestsCount(): Flow<Result<Int>> {
        return flow {
            emit(Result.success(3))
        }
    }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when reselecting the same item
            launchSingleTop = true
            // Restore state when re selecting previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.DASHBOARD -> navController.navigateToDashboard(topLevelNavOptions)
            TopLevelDestination.FRIENDS -> navController.navigateToFriends(topLevelNavOptions)
            TopLevelDestination.SETTINGS -> navController.navigateToSettings(topLevelNavOptions)
        }

    }
}