package id.my.ariqnf.temuantelyu.ui.post

import android.net.Uri
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    private val _uploadState = Channel<Resource<UiText>>()
    private val _errorState = Channel<CreatePostErrorState>()
    private val userId = Firebase.auth.currentUser?.uid
    val uiState = _uiState.asStateFlow()
    val errorState = _errorState.receiveAsFlow()
    val uploadState = _uploadState.receiveAsFlow()
    var isLoading by mutableStateOf(false)
        private set

    fun upload() {
        userId?.let {
            viewModelScope.launch {
                if (validateForm()) return@launch
                isLoading = true
                repository.uploadPost(
                    userId = it,
                    title = _uiState.value.title,
                    description = _uiState.value.description,
                    cate = _uiState.value.cate,
                    tags = splitTags(),
                    imageUri = _uiState.value.imageUri,
                ).let { result ->
                    when (result) {
                        is Resource.Error -> _uploadState.send(Resource.Error(data = UiText.StringResource(R.string.fail_upload), message = ""))
                        is Resource.Success -> {
                            _uiState.update { CreatePostUiState() }
                            _uploadState.send(Resource.Success(UiText.StringResource(R.string.post_uploaded)))
                        }
                    }
                }
                isLoading = false
            }
        }
    }

    private suspend fun validateForm(): Boolean {
        var error = CreatePostErrorState()
        var result = false
        if (_uiState.value.title.isBlank() || _uiState.value.title.length >= 255) {
            error = error.copy(title = UiText.StringResource(R.string.title_invalid))
            result = true
            _errorState.send(error)
        }

        if (_uiState.value.description.isBlank() || _uiState.value.description.length >= 1000) {
            error = error.copy(description = UiText.StringResource(R.string.description_invalid))
            result = true
            _errorState.send(error)
        }

        if (splitTags().isEmpty() || _uiState.value.tags.isBlank()) {
            error = error.copy(tags = UiText.StringResource(R.string.tags_invalid))
            result = true
            _errorState.send(error)
        }

        return result
    }

    fun setCate() {
        _uiState.update {
            if (it.cate == "lost") {
                it.copy(cate = "found")
            } else {
                it.copy(cate = "lost")
            }
        }
    }

    fun setTitle(value: String) {
        _uiState.update {
            it.copy(title = value)
        }
    }

    fun setDescription(value: String) {
        _uiState.update {
            it.copy(description = value)
        }
    }

    fun setTags(value: String) {
        _uiState.update {
            it.copy(tags = value)
        }
    }

    private fun splitTags(): List<String> {
        return _uiState.value.tags.split(", ", ",")
    }

    fun setImageUri(uri: Uri?) {
        _uiState.update {
            it.copy(
                imageUri = uri
            )
        }
    }
}