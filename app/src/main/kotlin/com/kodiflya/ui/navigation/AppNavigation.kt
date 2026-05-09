package com.kodiflya.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kodiflya.core.plugin.Category
import com.kodiflya.ui.component.NavigationIcons
import com.kodiflya.ui.screens.graph.GraphScreen
import com.kodiflya.ui.screens.home.HomeScreen
import com.kodiflya.ui.screens.sorting.SortingScreen
import com.kodiflya.ui.screens.splash.SplashScreen
import com.kodiflya.ui.screens.search.SearchScreen
import com.kodiflya.ui.screens.trees.TreeScreen

private const val ROUTE_HOME = "home"

private data class NavigationTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
)

private val navigationTabs = listOf(
    NavigationTab(ROUTE_HOME,             "Home",  NavigationIcons.HomeOutlined,  NavigationIcons.HomeFilled),
    NavigationTab(Category.SORTING.route, "Sort",  NavigationIcons.SortOutlined,  NavigationIcons.SortFilled),
    NavigationTab(Category.GRAPH.route,   "Graph", NavigationIcons.GraphOutlined, NavigationIcons.GraphFilled),
    NavigationTab(Category.TREES.route,   "Tree",   NavigationIcons.TreesOutlined,   NavigationIcons.TreesFilled),
    NavigationTab(Category.SEARCH.route,  "Search", NavigationIcons.SearchOutlined,  NavigationIcons.SearchFilled),
)

@Composable
private fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    unselectedIconColor = MaterialTheme.colorScheme.outlineVariant,
    unselectedTextColor = MaterialTheme.colorScheme.outlineVariant,
    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
)

@Composable
fun AppNavigation() {
    var splashDone by remember { mutableStateOf(false) }

    if (!splashDone) {
        SplashScreen(onDone = { splashDone = true })
        return
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                navigationTabs.forEach { tab ->
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) tab.selectedIcon else tab.icon,
                                contentDescription = tab.label,
                            )
                        },
                        label = { Text(tab.label) },
                        colors = navigationBarItemColors(),
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(ROUTE_HOME) {
                HomeScreen(onNavigate = { category ->
                    navController.navigate(category.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable(Category.SORTING.route) { SortingScreen() }
            composable(Category.GRAPH.route)   { GraphScreen() }
            composable(Category.TREES.route)   { TreeScreen() }
            composable(Category.SEARCH.route)  { SearchScreen() }
        }
    }
}
