package id.my.ariqnf.temuantelyu.ui.post

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import id.my.ariqnf.temuantelyu.LocalSnackbarHostState
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.widgets.BasicTopBar
import id.my.ariqnf.temuantelyu.ui.widgets.PostCard
import id.my.ariqnf.temuantelyu.ui.widgets.ReplyField
import id.my.ariqnf.temuantelyu.util.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostRepliesScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: PostRepliesViewModel = hiltViewModel()
) {
    val postState by viewModel.postState.collectAsState()
    val repliesState by viewModel.repliesState.collectAsState()
    val errorState by viewModel.errorState.collectAsState(initial = UiText.DynamicString(""))
    val snackbarHostState = LocalSnackbarHostState.current

    Scaffold(
        modifier = modifier,
        topBar = {
            BasicTopBar(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            ReplyField(
                value = viewModel.userReply,
                onValueChange = viewModel::setReply,
                onSend = {
                    viewModel.sendReply()
                }
            )
        }
    ) {
        LazyColumn(contentPadding = it, modifier = Modifier.padding(vertical = 8.dp)) {
            item {
                PostCard(
                    modifier = Modifier.padding(bottom = 16.dp),
                    post = postState.post,
                    cardShape = RoundedCornerShape(0.dp)
                ) {
                    IconButton(onClick = { TODO("Onclick DM") }) {
                        Icon(
                            painter = painterResource(R.drawable.mail),
                            contentDescription = stringResource(R.string.direct_message)
                        )
                    }
                }
            }
            items(repliesState.replies) { reply ->
                ReplyCard(reply = reply, modifier = Modifier.padding(bottom = 12.dp))
            }
        }
    }

    val replyErr = errorState.asString()
    LaunchedEffect(key1 = errorState) {
        if (replyErr.isNotBlank()) {
            snackbarHostState.showSnackbar(replyErr)
        }
    }
}