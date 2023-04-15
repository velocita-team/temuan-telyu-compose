package id.my.ariqnf.temuantelyu.ui.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.domain.PostRepository
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private val userId = Firebase.auth.currentUser?.uid
    private val _uiState = MutableStateFlow(MyPostUiState())
    val uiState = _uiState.asStateFlow()
    var openDialog by mutableStateOf(false)
        private set

    init {
        loadData()
    }

    fun toggleDialog() {
        openDialog = !openDialog
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getMyPosts(userId!!).let { result ->
                when (result) {
                    is Resource.Error -> _uiState.emit(MyPostUiState(error = UiText.StringResource(R.string.fetch_error)))
                    is Resource.Success -> result.data?.collect {
                        _uiState.emit(MyPostUiState(it))
                    }
                }
            }
        }
    }

    fun deletePost(documentId: String) {
        viewModelScope.launch {
            repository.deletePost(userId!!, documentId).let { result ->
                when (result) {
                    is Resource.Error -> _uiState.emit(MyPostUiState(error = UiText.StringResource(R.string.fail_delete)))
                    is Resource.Success -> return@launch
                }
            }
        }
    }
}