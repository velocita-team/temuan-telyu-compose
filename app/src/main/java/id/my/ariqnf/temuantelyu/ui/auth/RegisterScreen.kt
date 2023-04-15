package id.my.ariqnf.temuantelyu.ui.auth

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.rememberImeState
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    val uiState = viewModel.uiState
    val registerState = viewModel.registerState.collectAsState(initial = null)
    val errorState = viewModel.errorState.collectAsState(initial = RegisterErrorState())
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val icon = if (uiState.passwordVisible) R.drawable.visibility_off else R.drawable.visibility

    val nameErr = errorState.value.fullName.asString()
    val emailErr = errorState.value.email.asString()
    val passErr = errorState.value.password.asString()
    val rePassErr = errorState.value.rePassword.asString()

//  Scroll content when keyboard is shown
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, animationSpec = tween(250))
        }
    }

//  Content
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.temuan_telyu_logo),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(
                    RoundedCornerShape(4.dp)
                )
        )
        Spacer(modifier = Modifier.height(80.dp))
        AuthInputGroup(
            label = stringResource(R.string.full_name),
            value = uiState.fullName,
            onValueChange = viewModel::setFullName,
            hint = nameErr.ifBlank { stringResource(R.string.full_name_hint) },
            isError = nameErr.isNotBlank(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        AuthInputGroup(
            label = emailErr.ifBlank { stringResource(R.string.email) },
            value = uiState.email,
            onValueChange = viewModel::setEmail,
            hint = emailErr.ifBlank { stringResource(R.string.email_hint) },
            isError = emailErr.isNotBlank(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        AuthInputGroup(
            label = stringResource(R.string.password),
            value = uiState.password,
            onValueChange = viewModel::setPassword,
            hint = passErr.ifBlank { stringResource(R.string.password_hint) },
            isError = passErr.isNotBlank(),
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.togglePassVisibility()
                }) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = stringResource(
                            R.string.password_visible,
                            uiState.passwordVisible
                        )
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        AuthInputGroup(
            label = stringResource(R.string.repeat_password),
            value = uiState.rePassword,
            onValueChange = viewModel::setRePassword,
            hint = rePassErr.ifBlank { stringResource(R.string.repeat_password_hint) },
            isError = rePassErr.isNotBlank(),
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.togglePassVisibility()
                }) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = stringResource(
                            R.string.password_visible,
                            uiState.passwordVisible
                        )
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    focusManager.clearFocus()
                    scope.launch {
                        viewModel.registerUser()
                    }
                }
            ),
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        OutlinedButton(
            onClick = {
                scope.launch {
                    viewModel.registerUser()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = stringResource(R.string.register).uppercase())
            }
        }
    }

    LaunchedEffect(key1 = registerState.value) {
        when (registerState.value) {
            is Resource.Error -> {
                val msg = registerState.value?.message
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }

            is Resource.Success -> navigateToHome()
            null -> return@LaunchedEffect
        }
    }
}