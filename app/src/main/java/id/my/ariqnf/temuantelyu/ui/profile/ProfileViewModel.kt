package id.my.ariqnf.temuantelyu.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.data.User
import id.my.ariqnf.temuantelyu.domain.ProfileRepository
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository) :
    ViewModel() {

    val auth = Firebase.auth
    private val _userState = MutableStateFlow(ProfileUiState(User("Telyutizen")))
    val userState = _userState.asStateFlow()
    var logoutMsg by mutableStateOf<UiText?>(null)
        private set


    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            auth.currentUser?.uid?.let {
                repository.getUserName(it).let { result ->
                    when (result) {
                        is Resource.Error -> _userState.emit(
                            ProfileUiState(
                                User("Telyutizen"),
                                UiText.StringResource(R.string.fetch_error)
                            )
                        )

                        is Resource.Success -> _userState.emit(
                            ProfileUiState(
                                User(
                                    result.data,
                                    auth.currentUser?.email
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun logOut() {
        logoutMsg = try {
            auth.signOut()
            UiText.StringResource(R.string.have_logout)
        } catch (e: FirebaseAuthException) {
            UiText.DynamicString(e.toString())
        }
    }
}