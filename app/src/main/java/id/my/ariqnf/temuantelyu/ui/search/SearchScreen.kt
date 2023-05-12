package id.my.ariqnf.temuantelyu.ui.search

import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.widgets.LostFoundButton
import id.my.ariqnf.temuantelyu.ui.widgets.PostCard
import id.my.ariqnf.temuantelyu.util.Screen
import id.my.ariqnf.temuantelyu.util.formatDate
import id.my.ariqnf.temuantelyu.util.rememberImeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.postsState.collectAsState()
    val context = LocalContext.current
    val imeState = rememberImeState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = imeState.value) {
        if (!imeState.value && viewModel.undoClear.isNotBlank()) {
            viewModel.setSearch(viewModel.undoClear)
            focusManager.clearFocus()
        } else if (!imeState.value) {
            focusManager.clearFocus()
        }
    }

    val isFound = uiState.cateFilter == "found"
    val isLost = uiState.cateFilter == "lost"
    Scaffold(
        topBar = {
            SearchTopBar(
                value = viewModel.searchText,
                onValueChange = viewModel::setSearch,
                onBack = { navController.popBackStack() },
                onSearch = viewModel::loadData,
                onClear = viewModel::clearSearch
            )
        },
        modifier = modifier
    ) {
        LazyColumn(contentPadding = it) {
            item {
                LostFoundButton(
                    onLostClick = { viewModel.filterPost("lost") },
                    onFoundClick = { viewModel.filterPost("found") },
                    lostChecked = isLost,
                    foundChecked = isFound,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 20.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    role = Role.Checkbox
                )
            }
            items(uiState.data) { post ->
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

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(navController = rememberNavController())
}