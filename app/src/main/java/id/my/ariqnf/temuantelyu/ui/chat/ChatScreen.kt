package id.my.ariqnf.temuantelyu.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import id.my.ariqnf.temuantelyu.LocalSnackbarHostState
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.ui.widgets.BasicTopBar
import id.my.ariqnf.temuantelyu.ui.widgets.ReplyField
import id.my.ariqnf.temuantelyu.util.UiText

@Composable
fun ChatScreenContainer(
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state = viewModel.chatUiState.collectAsState()
    val errorState = viewModel.errorState.collectAsState(initial =  UiText.DynamicString(""))
    val errorMsg = errorState.value.asString()
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(key1 = errorState.value) {
        if (errorMsg.isNotBlank())
            snackbarHostState.showSnackbar(errorMsg)
    }
    ChatScreen(
        chatUiState = state.value,
        replyValue = viewModel.replyValue,
        onReplyChange = viewModel::updateReplyValue,
        navigateBack = { navController.popBackStack() },
        onSend = viewModel::sendChat
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatUiState: ChatUiState,
    replyValue: String,
    onReplyChange: (String) -> Unit,
    onSend: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            BasicTopBar(navigateBack = navigateBack, title = chatUiState.otherUserName)
        },
        bottomBar = {
            ReplyField(value = replyValue, onValueChange = onReplyChange, onSend = onSend)
        },
        modifier = modifier
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.padding(top = 16.dp, end = 8.dp, bottom = 8.dp, start = 8.dp),
        ) {
            chatUiState.data.forEach { group ->
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = group.key,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                items(group.value) {
                    val chatAlignment = if (it.sender == chatUiState.currentUserId) Alignment.End else Alignment.Start
                    val chatColor =
                        if (it.sender == chatUiState.currentUserId)
                            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        else
                            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ChatBubble(
                        chat = it,
                        modifier = Modifier.padding(bottom = 10.dp),
                        horizontalAlignment = chatAlignment,
                        colors = chatColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    TemuanTelyuTheme {
        ChatScreen(ChatUiState(),"", {}, {}, {})
    }
}