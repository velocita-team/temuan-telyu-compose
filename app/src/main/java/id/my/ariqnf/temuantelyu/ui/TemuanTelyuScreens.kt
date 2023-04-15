package id.my.ariqnf.temuantelyu.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.my.ariqnf.temuantelyu.ui.auth.LoginScreen
import id.my.ariqnf.temuantelyu.ui.auth.RegisterScreen
import id.my.ariqnf.temuantelyu.ui.home.HomeScreen
import id.my.ariqnf.temuantelyu.ui.post.MyPostScreen
import id.my.ariqnf.temuantelyu.ui.profile.ProfileScreen
import id.my.ariqnf.temuantelyu.util.Screen

@Composable
fun TemuanTelyuScreens(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.MyPost.route) {
            MyPostScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(
                navigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(navigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            })
        }
    }
}