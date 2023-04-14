package id.my.ariqnf.temuantelyu.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import id.my.ariqnf.temuantelyu.LocalCoroutineScope
import id.my.ariqnf.temuantelyu.LocalSnackbarHostState
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.widgets.Navbar
import id.my.ariqnf.temuantelyu.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = LocalCoroutineScope.current
    val logoutMsg = viewModel.logoutMsg?.asString()

    Scaffold(
        bottomBar = { Navbar(navController) },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            ProfileBanner(userState.user.fullName ?: "Telyutizen", userState.user.email ?: "")
            Spacer(modifier = Modifier.height(24.dp))
            if (viewModel.auth.currentUser?.isAnonymous == false) {
                ProfileMenu(
                    iconRes = R.drawable.article,
                    labelRes = R.string.my_posts,
                    trailingIconRes = R.drawable.arrow_forward_ios,
                    borderWidth = 1.dp,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.MyPost.route)
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
                ProfileMenu(
                    iconRes = R.drawable.logout,
                    labelRes = R.string.logout,
                    color = MaterialTheme.colorScheme.primary,
                    borderWidth = 1.dp,
                    modifier = Modifier.clickable {
                        viewModel.logOut()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(logoutMsg!!)
                        }
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            } else {
                ProfileMenu(
                    iconRes = R.drawable.login,
                    labelRes = R.string.login,
                    borderWidth = 1.dp,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
        }
    }
}