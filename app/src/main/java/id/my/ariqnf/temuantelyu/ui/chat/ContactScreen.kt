package id.my.ariqnf.temuantelyu.ui.chat

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import id.my.ariqnf.temuantelyu.LocalSnackbarHostState
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.data.Chat
import id.my.ariqnf.temuantelyu.data.Contact
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.ui.widgets.BasicTopBar
import id.my.ariqnf.temuantelyu.util.Screen

@Composable
fun ContactScreenContainer(
    navController: NavHostController,
    viewModel: ContactViewModel = hiltViewModel()
) {
    val state = viewModel.contactUiState.collectAsState()
    val errorState = viewModel.errorState.collectAsState(initial = ContactUiState())
    val errorMsg = errorState.value.error?.asString() ?: ""
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(key1 = errorState.value) {
        if (errorMsg.isNotBlank())
            snackbarHostState.showSnackbar(errorMsg)
    }
    
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    ContactScreen(
        contacts = state.value.data,
        onNavigate = { navController.popBackStack() },
        onContactClick = { navController.navigate(Screen.Chat.route + "/$it") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    contacts: List<Contact>,
    onNavigate: () -> Unit,
    onContactClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        BasicTopBar(
            navigateBack = onNavigate,
            title = stringResource(R.string.messages)
        )
    }, modifier = modifier) { padding ->
        LazyColumn(contentPadding = padding) {
            items(contacts) {
                ContactList(contact = it, onContactClick = { onContactClick(it.id) })
            }
        }
    }
}

@Preview
@Composable
private fun ContactScreenPreview() {
    TemuanTelyuTheme {
        ContactScreen(contacts = contactListDummy, {}, {})
    }
}

private val contactListDummy = listOf(
    Contact(
        "1",
        "Leon",
        Chat("Ada", "Halo saya kembali")
    ),
    Contact(
        "2",
        "Ashley",
        Chat("Leon", "Pergi kemana si?")
    )
)