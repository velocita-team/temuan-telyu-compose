package id.my.ariqnf.temuantelyu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import id.my.ariqnf.temuantelyu.domain.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    var isLoading by mutableStateOf(true)
        private set
    private lateinit var auth: FirebaseAuth

    init {
        viewModelScope.launch {
            auth = Firebase.auth
            if (auth.currentUser == null) {
                repository.createAnonymous()
            }
            delay(1500)
            isLoading = false
        }
    }
}