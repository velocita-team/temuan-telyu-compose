package id.my.ariqnf.temuantelyu.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import id.my.ariqnf.temuantelyu.domain.AuthRepository
import id.my.ariqnf.temuantelyu.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val auth = Firebase.auth
    var uiState by mutableStateOf(LoginUiState())
        private set
    var isLoading by mutableStateOf(false)
        private set

    private val _loginState = Channel<Resource<AuthResult>>()
    val loginState = _loginState.receiveAsFlow()

    fun setEmail(value: String) {
        uiState = uiState.copy(email = value)
    }

    fun setPassword(value: String) {
        uiState = uiState.copy(password = value)
    }

    fun togglePassVisibility() {
        uiState = uiState.copy(passwordVisible = !uiState.passwordVisible)
    }

    fun loginUser() = viewModelScope.launch {
        isLoading = true
        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()
        }
        repository.loginUser(uiState.email, uiState.password).let { result ->
                _loginState.send(result)
            }
        isLoading = false
    }
}