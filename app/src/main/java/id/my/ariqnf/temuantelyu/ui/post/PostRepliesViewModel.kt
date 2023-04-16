package id.my.ariqnf.temuantelyu.ui.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.domain.PostRepliesRepository
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostRepliesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PostRepliesRepository
) : ViewModel() {

    private val currentUser = Firebase.auth.currentUser
    private val userId: String = checkNotNull(savedStateHandle["userId"])
    private val postId: String = checkNotNull(savedStateHandle["postId"])
    private val _postState = MutableStateFlow(PostRepliesUiState.PostState())
    private val _repliesState = MutableStateFlow(PostRepliesUiState.RepliesState())
    private val _errorState = Channel<UiText>()
    val postState = _postState.asStateFlow()
    val repliesState = _repliesState.asStateFlow()
    val errorState = _errorState.receiveAsFlow()
    var userReply by mutableStateOf("")
        private set

    init {
        loadData()
    }

    fun setReply(value: String) {
        userReply = value
    }

    private fun loadData() = viewModelScope.launch {
        repository.getPost(userId, postId).let { result ->
            when (result) {
                is Resource.Error -> _postState.emit(
                    PostRepliesUiState.PostState(error = UiText.DynamicString(result.message!!))
                )

                is Resource.Success -> _postState.emit(
                    PostRepliesUiState.PostState(result.data!!)
                )
            }
        }

        repository.getReplies(userId, postId).let { result ->
            when (result) {
                is Resource.Error -> _repliesState.emit(
                    PostRepliesUiState.RepliesState(error = UiText.DynamicString(result.message!!))
                )

                is Resource.Success -> result.data?.collect {
                    _repliesState.emit(
                        PostRepliesUiState.RepliesState(it)
                    )
                }
            }
        }
    }


    fun sendReply() = viewModelScope.launch {
        if (currentUser!!.isAnonymous) {
            _errorState.send(UiText.StringResource(R.string.limited_access))
            return@launch
        }

        if (userReply.isNotBlank() && userReply.length <= 500) {
            repository.sendReply(userId, postId, currentUser.uid, userReply)
            userReply = ""
        } else {
            _errorState.send(UiText.StringResource(R.string.reply_invalid))
        }
    }
}