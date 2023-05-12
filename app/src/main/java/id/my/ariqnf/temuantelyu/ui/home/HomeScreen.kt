package id.my.ariqnf.temuantelyu.ui.home

import android.content.Intent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import id.my.ariqnf.temuantelyu.LocalSnackbarHostState
import id.my.ariqnf.temuantelyu.LocalSystemUiController
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.theme.Gray950
import id.my.ariqnf.temuantelyu.ui.theme.Red600
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.ui.theme.WhiteSmoke
import id.my.ariqnf.temuantelyu.ui.widgets.Navbar
import id.my.ariqnf.temuantelyu.ui.widgets.PostCard
import id.my.ariqnf.temuantelyu.util.Screen
import id.my.ariqnf.temuantelyu.util.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
     viewModel: HomeViewModel = hiltViewModel()
) {
    val systemUiController = LocalSystemUiController.current
    val postsUiState by viewModel.postsState.collectAsState()
    val userName by viewModel.user.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current
    val (postErr, userNameErr) = listOf(postsUiState.error?.asString(), userName.error?.asString())
    val statusBarColor = if (isSystemInDarkTheme()) Gray950 else WhiteSmoke
    // Change android status bar color
    DisposableEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = Red600
        )

        onDispose {
            systemUiController.setStatusBarColor(
                color = statusBarColor
            )
        }
    }

    LaunchedEffect(key1 = postsUiState.error, key2 = userName.error) {
        if (!postErr.isNullOrBlank()) {
            snackbarHostState.showSnackbar(postErr)
        }
        if (!userNameErr.isNullOrBlank()) {
            snackbarHostState.showSnackbar(userNameErr)
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                userName = userName.name!!,
                value = viewModel.searchText,
                onValueChange = viewModel::setSearch,
                onSearch = {
                    if (viewModel.searchText.isNotBlank()) {
                        navController.navigate(Screen.Search.route + "/${viewModel.searchText}")
                        viewModel.clearSearch()
                    }
                }
            )
        },
        bottomBar = {
            Navbar(navController)
        },

        modifier = modifier
    ) {
        LazyColumn(contentPadding = it) {
            items(postsUiState.data) { post ->
                val shareMsg = stringResource(
                    R.string.share_user,
                    post.cate!!,
                    post.title!!,
                    post.sender!!,
                    post.date!!.formatDate(),
                    post.content!!
                )
                PostCard(
                    post,
                    Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    IconButton(onClick = {
                        navController.navigate(Screen.Post.route + "/${post.id}")
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.sms),
                            contentDescription = stringResource(R.string.comment)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareMsg)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    TemuanTelyuTheme() {
        HomeScreen(navController = rememberNavController())
    }
}
