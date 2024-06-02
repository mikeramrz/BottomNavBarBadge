package com.example.bottomnavbarbadge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.bottomnavbarbadge.FriendsRoute


const val FRIENDS_ROUTE = "friends_route"


fun NavController.navigateToFriends(navOptions: NavOptions) =
    navigate(FRIENDS_ROUTE, navOptions)

fun NavGraphBuilder.friendsScreen(
) {
    composable(
        route = FRIENDS_ROUTE,
    ) {
        FriendsRoute()
    }
}
