package id.my.ariqnf.temuantelyu.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import id.my.ariqnf.temuantelyu.data.Chat
import id.my.ariqnf.temuantelyu.domain.ChatRepository
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.UiText
import id.my.ariqnf.temuantelyu.util.formatDate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ChatRepository
) : ViewModel() {

    private val currentUser = Firebase.auth.currentUser
    private val otherUserId: String = checkNotNull(savedStateHandle["otherUserId"])
    private val _chatUiState = MutableStateFlow(ChatUiState())
    private val _errorState = Channel<ChatUiState>()
    private var chatRoomId by mutableStateOf("")
    val chatUiState = _chatUiState.asStateFlow()
    val errorState = _errorState.receiveAsFlow()
    var replyValue by mutableStateOf("")
        private set

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        currentUser?.uid?.let { userId ->
            repository.getChats(userId, otherUserId).let { result ->
                when (result) {
                    is Resource.Error -> _errorState.send(ChatUiState(error = UiText.DynamicString(result.message!!)))
                    is Resource.Success -> result.data?.let { chatRoom ->
                        chatRoomId = chatRoom.roomId
                        chatRoom.result.collect { chatList ->
                            _chatUiState.emit(
                                ChatUiState(
                                    userId,
                                    chatRoom.otherUserName,
                                    chatList.groupBy { it.timestamp.formatDate() }
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun sendChat() = viewModelScope.launch {
        repository.sendReply(
            chatRoomId,
            Chat(
                sender = _chatUiState.value.currentUserId,
                message = replyValue
            )
        )
        replyValue = ""
    }

    fun updateReplyValue(value: String) {
        replyValue = value
    }
}