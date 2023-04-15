package id.my.ariqnf.temuantelyu.ui.post

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import id.my.ariqnf.temuantelyu.LocalSnackbarHostState
import id.my.ariqnf.temuantelyu.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MyPostViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(MyPostUiState())
    val snackbarHostState = LocalSnackbarHostState.current
    val failMsg = uiState.error?.asString()
    var currentPostId by remember { mutableStateOf("") }

    LaunchedEffect(key1 = uiState.error) {
        if (uiState.error != null)
            snackbarHostState.showSnackbar(failMsg!!)
    }

    Scaffold(
        topBar = {
            PostTopAppBar(titleRes = R.string.post, navigateBack = {
                navController.popBackStack()
            })
        },
        modifier = modifier
    ) {
        LazyColumn(contentPadding = it) {
            items(uiState.data) { post ->
                MyPostCard(
                    post = post,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = {
                        currentPostId = post.id!!
                        viewModel.toggleDialog()
                    }
                )
            }
        }
    }

    if (viewModel.openDialog) {
        AlertDialog(
            tonalElevation = 0.5.dp,
            onDismissRequest = { viewModel.toggleDialog() },
            title = { Text(stringResource(R.string.ask_confirmation)) },
            text = { Text(stringResource(R.string.delete_info)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletePost(currentPostId)
                    viewModel.toggleDialog()
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.toggleDialog()}) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}