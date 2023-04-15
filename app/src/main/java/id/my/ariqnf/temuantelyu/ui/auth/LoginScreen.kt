package id.my.ariqnf.temuantelyu.ui.auth

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
fun LoginScreen(
    navigateToRegister: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    val uiState = viewModel.uiState
    val loginState = viewModel.loginState.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val icon = if (uiState.passwordVisible) R.drawable.visibility_off else R.drawable.visibility

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
            label = stringResource(R.string.email),
            value = uiState.email,
            onValueChange = viewModel::setEmail,
            hint = stringResource(R.string.email_hint),
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
            hint = stringResource(R.string.password_hint),
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
                        viewModel.loginUser()
                    }
                }
            ),
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        Button(
            onClick = {
                scope.launch {
                    viewModel.loginUser()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(text = stringResource(R.string.login).uppercase())
            }
        }
        OutlinedButton(
            onClick = navigateToRegister,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(text = stringResource(R.string.register).uppercase())
        }
    }

    LaunchedEffect(key1 = loginState.value) {
        when (loginState.value) {
            is Resource.Error -> {
                val msg = loginState.value?.message
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }

            is Resource.Success -> navigateToHome()
            null -> return@LaunchedEffect
        }
    }
}