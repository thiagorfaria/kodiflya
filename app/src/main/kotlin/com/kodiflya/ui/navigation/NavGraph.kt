package com.kodiflya.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kodiflya.core.plugin.Category
import com.kodiflya.ui.graph.GraphScreen
import com.kodiflya.ui.home.HomeScreen
import com.kodiflya.ui.sorting.SortingScreen
import com.kodiflya.ui.trees.TreeScreen
import com.kodiflya.ui.theme.AccentGreen
import com.kodiflya.ui.theme.Background
import com.kodiflya.ui.theme.Surface
import com.kodiflya.ui.theme.TextPrimary
import com.kodiflya.ui.theme.MetricLabel as MetricLabelColor

// Parameterised route pattern: future categories are registry entries, not new routes.
private const val ROUTE_HOME  = "home"
private const val ROUTE_SORT  = "sort"
private const val ROUTE_GRAPH = "graph"
private const val ROUTE_TREE  = "tree"

private data class NavTab(val route: String, val label: String, val icon: String)

private val tabs = listOf(
    NavTab(ROUTE_HOME,  "Home",  "⌂"),
    NavTab(ROUTE_SORT,  "Sort",  "↑↓"),
    NavTab(ROUTE_GRAPH, "Graph", "◉"),
    NavTab(ROUTE_TREE,  "Tree",  "⑂"),
)

@Composable
fun KodiflyaNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavigationBar(
                containerColor = Surface,
                contentColor = TextPrimary,
            ) {
                tabs.forEach { tab ->
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
                        icon = { Text(tab.icon) },
                        label = { Text(tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentGreen,
                            selectedTextColor = AccentGreen,
                            unselectedIconColor = MetricLabelColor,
                            unselectedTextColor = MetricLabelColor,
                            indicatorColor = Background,
                        ),
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
                    val route = when (category) {
                        Category.SORTING -> ROUTE_SORT
                        Category.GRAPH   -> ROUTE_GRAPH
                        Category.TREES   -> ROUTE_TREE
                    }
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable(ROUTE_SORT) { SortingScreen() }
            composable(ROUTE_GRAPH) { GraphScreen() }
            composable(ROUTE_TREE) { TreeScreen() }
        }
    }
}

@Composable
private fun PlaceholderScreen(label: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "$label — coming soon",
            color = MetricLabelColor,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
