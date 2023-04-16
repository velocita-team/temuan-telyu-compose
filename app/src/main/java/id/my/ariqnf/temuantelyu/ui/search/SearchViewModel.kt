package id.my.ariqnf.temuantelyu.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.domain.SearchRepository
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _postData = MutableStateFlow<List<Post>>(emptyList())
    private val _uiState = MutableStateFlow(SearchUiState())
    val postsState = _uiState.asStateFlow()

    var searchText by mutableStateOf<String>(checkNotNull(savedStateHandle["search"]))
        private set
    var undoClear by mutableStateOf("")
        private set

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getAllPostsWhereTags(searchText.split(", ", ",")).let { result ->
                when (result) {
                    is Resource.Error -> _uiState.emit(
                        SearchUiState(
                            error = UiText.StringResource(
                                R.string.fetch_error
                            )
                        )
                    )

                    is Resource.Success -> result.data?.collect {
                        _uiState.emit(SearchUiState(it))
                        _postData.emit(it)
                    }
                }
            }
        }
    }

    fun setSearch(value: String) {
        searchText = value
    }

    fun clearSearch() {
        undoClear = searchText
        searchText = ""
    }

    fun filterPost(cate: String) {
        viewModelScope.launch {
            if (_uiState.value.cateFilter == cate) {
                _uiState.update {
                    it.copy(data = _postData.value, cateFilter = "")
                }
            } else {
                _uiState.update {
                    it.copy(
                        cateFilter = cate,
                        data = _postData.value.filter { post ->
                            post.cate == cate
                        }
                    )
                }
            }
        }
    }
}