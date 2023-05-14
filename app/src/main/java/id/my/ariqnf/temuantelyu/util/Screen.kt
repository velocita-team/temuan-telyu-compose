package id.my.ariqnf.temuantelyu.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import id.my.ariqnf.temuantelyu.R

sealed class Screen(
    val route: String,
    val params: String? = null,
    @StringRes val labelRes: Int? = null,
    val icon: ImageVector? = null
) {
    object Home : Screen("home", labelRes = R.string.home, icon = Icons.Default.Home)
    object CreatePost : Screen("create-post", labelRes = R.string.create_post, icon = Icons.Default.Add)
    object Profile : Screen("profile", labelRes = R.string.profile, icon = Icons.Default.Person)
    object Post : Screen("post", "/users/{userId}/posts/{postId}")
    object Search : Screen("search", "/{search}")
    object MyPost : Screen("my-post")
    object Login : Screen("login")
    object Register : Screen("register")
    object Chat : Screen("chat", "/{otherUserId}")
    object Contact : Screen("contact")
}
