package id.my.ariqnf.temuantelyu.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import id.my.ariqnf.temuantelyu.domain.ChatRepository
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {

    private val currentUser = Firebase.auth.currentUser
    private val _contactUiState = MutableStateFlow(ContactUiState())
    private val _errorState = Channel<ContactUiState>()
    val contactUiState = _contactUiState.asStateFlow()
    val errorState = _errorState.receiveAsFlow()

    fun loadData() = viewModelScope.launch {
        currentUser?.uid?.let { userId ->
            repository.getMyChats(userId).let { result ->
                when (result) {
                    is Resource.Error -> _errorState.send(
                        ContactUiState(
                            error = UiText.DynamicString(
                                result.message!!
                            )
                        )
                    )

                    is Resource.Success -> result.data?.collect { list ->
                        _contactUiState.emit(
                            ContactUiState(
                                data = list.sortedByDescending { it.latestChat.timestamp }
                            )
                        )
                    }
                }
            }
        }
    }
}