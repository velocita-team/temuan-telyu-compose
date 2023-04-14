package id.my.ariqnf.temuantelyu.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.domain.HomeRepository
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {
    private val currentUser = Firebase.auth.currentUser

    var searchText by mutableStateOf("")
        private set

    private val _user = MutableStateFlow(HomeUiState.User())
    val user = _user.asStateFlow()

    private val _postsState = MutableStateFlow(HomeUiState.Posts())
    val postsState = _postsState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            currentUser?.uid?.let {
                if (!currentUser.isAnonymous)
                    _user.emit(repository.getUserName(it).let { result ->
                        when (result) {
                            is Resource.Error -> HomeUiState.User(error = UiText.StringResource(R.string.fetch_error))
                            is Resource.Success -> HomeUiState.User(result.data)
                        }
                    })
            }

            repository.getAllPost().let { result ->
                when (result) {
                    is Resource.Error -> _postsState.emit(
                        HomeUiState.Posts(
                            error = UiText.StringResource(
                                R.string.fetch_error
                            )
                        )
                    )

                    is Resource.Success -> result.data?.collect {
                        _postsState.emit(HomeUiState.Posts(it))
                    }
                }
            }
        }
    }

    fun setSearch(text: String) {
        searchText = text
    }

    fun clearSearch() {
        searchText = ""
    }
}