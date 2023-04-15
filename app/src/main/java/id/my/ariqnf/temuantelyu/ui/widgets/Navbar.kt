package id.my.ariqnf.temuantelyu.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.util.Screen

private val menuItems = listOf(
    Screen.Home,
    Screen.CreatePost,
    Screen.Profile
)

@Composable
fun Navbar(navController: NavHostController, modifier: Modifier = Modifier) {
    NavigationBar(modifier = modifier, containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 0.5.dp) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        menuItems.forEachIndexed { index, menuItem ->
            if (index == 1) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(menuItem.route)
                    },
                    icon = {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(12.dp)
                        ) {
                            Icon(
                                imageVector = menuItem.icon!!,
                                contentDescription = stringResource(menuItem.labelRes!!)
                            )
                        }
                    },
                    enabled = true,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            } else {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == menuItem.route } == true,
                    onClick = {
                        navController.navigate(menuItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {
                        Text(text = stringResource(menuItem.labelRes!!))
                    },
                    icon = {
                        Icon(imageVector = menuItem.icon!!, contentDescription = null)
                    },
                    enabled = true,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun NavbarPreview() {
    TemuanTelyuTheme(darkTheme = false) {
        Navbar(navController = rememberNavController())
    }
}