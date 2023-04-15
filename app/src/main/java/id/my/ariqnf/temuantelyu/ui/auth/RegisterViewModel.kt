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
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.domain.AuthRepository
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.UiText
import id.my.ariqnf.temuantelyu.util.isValidEmail
import id.my.ariqnf.temuantelyu.util.isValidName
import id.my.ariqnf.temuantelyu.util.isValidPassword
import id.my.ariqnf.temuantelyu.util.passwordMatches
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val auth = Firebase.auth
    var uiState by mutableStateOf(RegisterUiState())
        private set
    var isLoading by mutableStateOf(false)
        private set

    private val _registerState = Channel<Resource<AuthResult>>()
    val registerState = _registerState.receiveAsFlow()
    private val _errorState = Channel<RegisterErrorState>()
    val errorState = _errorState.receiveAsFlow()

    fun setEmail(value: String) {
        uiState = uiState.copy(email = value)
    }

    fun setPassword(value: String) {
        uiState = uiState.copy(password = value)
    }

    fun setRePassword(value: String) {
        uiState = uiState.copy(rePassword = value)
    }

    fun setFullName(value: String) {
        uiState = uiState.copy(fullName = value)
    }

    fun togglePassVisibility() {
        uiState = uiState.copy(passwordVisible = !uiState.passwordVisible)
    }

    fun registerUser() {
        viewModelScope.launch {
            if (validateForm()) return@launch

            isLoading = true
            if (auth.currentUser?.isAnonymous == true) {
                auth.currentUser?.delete()
            }
            repository.registerUser(uiState.email, uiState.password).let { result ->
                if (result is Resource.Success) {
                    repository.setUserFullName(Firebase.auth.currentUser?.uid!!, uiState.fullName)
                }
                _registerState.send(result)
            }
        }
        isLoading = false
    }

    private suspend fun validateForm(): Boolean {
        var error = RegisterErrorState()
        var result = false
        if (!uiState.fullName.isValidName()) {
            error = error.copy(fullName = UiText.StringResource(R.string.full_name_invalid))
            _errorState.send(error)
            result = true
        }

        if (!uiState.email.isValidEmail()) {
            error = error.copy(email = UiText.StringResource(R.string.email_invalid))
            _errorState.send(error)
            result = true
        }

        if (!uiState.password.isValidPassword()) {
            error = error.copy(password = UiText.StringResource(R.string.password_invalid))
            _errorState.send(error)
            result = true
        }

        if (!uiState.password.passwordMatches(uiState.rePassword)) {
            error = error.copy(rePassword = UiText.StringResource(R.string.password_match_invalid))
            _errorState.send(error)
            result = true
        }

        return result
    }
}

