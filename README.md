#### This is not a architecturaly correct example. Normally we would break things out into modules etc but this is one way to show bottom nav badges to inform users of specific actions or important events.


<img width="319" alt="Screenshot 2024-06-01 at 9 25 49 PM" src="https://github.com/mikeramrz/BottomNavBarBadge/assets/11188935/75628399-ec61-4a00-88ce-4b4ac752c0be">

##### We start by getting the data we need in our app state as such. Where we get a map of top level destinations and then attatch some value to them in a map.

```
 //Map of top level destinations, key is the route.
    val topLevelDestinations: List<TopLevelDestination> = buildList {
        add(TopLevelDestination.DASHBOARD)
        add(TopLevelDestination.FRIENDS)
        add(TopLevelDestination.SETTINGS)
    }

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
```

##### A real world example is by getting from our repository a count of unseen requests.
```val topLevelDestinationWithUnseenAlerts: StateFlow<Map<TopLevelDestination, Int>> =
        friendRequestRepository.getUnseenIncomingFriendRequestsCount()
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
                initialValue = emptyMap(),
            )
```
##### We can do this by injecting the repository into our AppState as such
```class BarBadgeAppState(
    val navController: NavHostController,
    friendRequestRepository: IFriendRequestRepository, <--- Injected
    coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
)
```
and in our main activity 
```  @Inject
    lateinit var friendRequestRepository: IFriendRequestRepository

setContent {
 val appState = rememberBarBadgeAppState(
windowSizeClass = calculateWindowSizeClass(this@MainActivity),
friendRequestRepository = friendRequestRepository,)
```

##### And then finally to consume the top level destinations with badges we put the map into the bottom bar or nav rail
```private fun BarBadgeBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    destinationsWithUnreadResources: Map<TopLevelDestination, Int>,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
)
```
We check if each destination has any unread or markers to show and use Material 3 badge box

```BarBadgeNavigationBar(
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
                }
```
 [bar_badge_example.webm](https://github.com/mikeramrz/BottomNavBarBadge/assets/11188935/cd22bb8f-783c-44b2-b3b7-8f8101a3449e)

##### You can even do this in tabs within screens
```
PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                tabDestinations.forEachIndexed { index, tabDestination ->
                    Tab(
                        icon = {
                            if (tabDestination == FriendScreenTabDestinations.REQUESTS && friendRequestsUiState is FriendRequestsUiState.Success
                                && friendRequestsUiState.friendRequests.any { it.seen.not() }
                            ) {
                                BadgedBox(
                                    badge = {
                                        Badge {
                                            Text(text = friendRequestsUiState.friendRequests.count { it.seen.not() }
                                                .toString())
                                        }
                                    }) {
                                    Icon(
                                        imageVector = tabDestination.selectedIcon,
                                    )
                                }

                            } else {
                                Icon(
                                    imageVector = tabDestination.selectedIcon,
                                )
                            }

                        },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(text = (stringResource(id = tabDestination.titleTextId)))
                        }
                    )
                }
            }
```

<img width="321" alt="Screenshot 2024-06-01 at 9 59 11 PM" src="https://github.com/mikeramrz/BottomNavBarBadge/assets/11188935/2bae692b-247e-436a-865d-ee5fede460b1">
