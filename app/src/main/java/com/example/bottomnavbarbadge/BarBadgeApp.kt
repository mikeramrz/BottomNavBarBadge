package com.example.bottomnavbarbadge

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.example.bottomnavbarbadge.components.BarBadgeNavigationBar
import com.example.bottomnavbarbadge.components.BarBadgeNavigationBarItem
import com.example.bottomnavbarbadge.navigation.TopLevelDestination

@Composable
fun BarBadgeApp(
    appState: BarBadgeAppState,
    modifier: Modifier = Modifier,
) {

    val snackbarHostState = remember { SnackbarHostState() }

    BarBadgeApp(appState = appState, snackbarHostState = snackbarHostState, modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BarBadgeApp(
    appState: BarBadgeAppState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    val unreadDestinations by appState.topLevelDestinationUnseenFriendRequestCountFlow.collectAsStateWithLifecycle()



    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                BarBadgeBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    destinationsWithUnreadResources = unreadDestinations,
                )
            }
        },

        ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                )
        ) {
            //We could have a nav rail here but we don't for simplicity

            Column(Modifier.fillMaxSize()) {

                val destination = appState.currentTopLevelDestination
                val shouldShowTopBar = destination != null

                val navController: NavHostController = appState.navController

                val isBackButtonVisible by remember {
                    derivedStateOf {
                        navController.previousBackStackEntry != null
                    }
                }

                BarBadgeNavHost(
                    appState = appState,
                    modifier = if (shouldShowTopBar) {
                        Modifier.consumeWindowInsets(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                        )
                    } else {
                        Modifier
                    }
                )

            }
        }
    }

}

@Composable
private fun BarBadgeBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    destinationsWithUnreadResources: Map<TopLevelDestination, Int>,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    BarBadgeNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->

            val hasUnread = destinationsWithUnreadResources.contains(destination)
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            BarBadgeNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    if (hasUnread) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    val badgeNumber = destinationsWithUnreadResources[destination]
                                    Text(badgeNumber.toString())
                                }
                            }
                        ) {
                            Icon(
                                imageVector = destination.unselectedIcon,
                                contentDescription = null,
                            )
                        }
                    } else {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = null,
                        )
                    }
                },
                selectedIcon = {
                    if (hasUnread) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    val badgeNumber = destinationsWithUnreadResources[destination]
                                    Text(badgeNumber.toString())
                                }
                            }
                        ) {
                            Icon(
                                imageVector = destination.selectedIcon,
                                contentDescription = null,
                            )
                        }
                    } else {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = null,
                        )
                    }
                },
                label = { Text(stringResource(destination.iconTextId)) },
            )
        }
    }
}



private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
