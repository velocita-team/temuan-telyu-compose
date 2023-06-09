package id.my.ariqnf.temuantelyu.ui.post

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import id.my.ariqnf.temuantelyu.LocalCoroutineScope
import id.my.ariqnf.temuantelyu.LocalSnackbarHostState
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.ui.widgets.LostFoundButton
import id.my.ariqnf.temuantelyu.util.Resource
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val uploadState = viewModel.uploadState.collectAsState(null)
    val errorState = viewModel.errorState.collectAsState(CreatePostErrorState())

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { viewModel.setImageUri(it) }
    )
//    val feedbackMsg = stringResource(viewModel._uploadState)
    val scope = LocalCoroutineScope.current
    val snackbarHostState = LocalSnackbarHostState.current

    // show snack-bar after post button clicked
    val uploadMsg = uploadState.value?.data?.asString()
    LaunchedEffect(key1 = uploadState.value) {
        when (uploadState.value) {
            is Resource.Error -> snackbarHostState.showSnackbar(uploadMsg!!)
            is Resource.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(uploadMsg!!)
                }
                navController.popBackStack()
            }

            null -> return@LaunchedEffect
        }
    }

    val titleErr = errorState.value.title.asString()
    val locationErr = errorState.value.location.asString()
    val descriptionErr = errorState.value.description.asString()
    val tagsErr = errorState.value.tags.asString()
    val isFound = uiState.value.cate == "found"
    val isLost = uiState.value.cate == "lost"

    Scaffold(
        modifier = modifier,
        topBar = {
            CreatePostTopBar(
                navigateBack = {
                    navController.popBackStack()
                },
                onPost = {
                    viewModel.upload()
                },
                enablePost = !viewModel.isLoading
            )
        },
        bottomBar = {
            UploadImgBottomBar(onUpload = {
                launcher.launch("image/*")
            }, imageUri = uiState.value.imageUri)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            LostFoundButton(
                onLostClick = viewModel::setCate,
                onFoundClick = viewModel::setCate,
                enableFound = isLost,
                enableLost = isFound,
                foundChecked = isFound,
                lostChecked = isLost,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Column {
                PostForm(
                    value = uiState.value.title,
                    onValueChange = { viewModel.setTitle(it) },
                    labelRes = R.string.title,
                    formHint = titleErr.ifBlank { stringResource(R.string.title_hint) },
                    isError = titleErr.isNotBlank(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                PostForm(
                    value = uiState.value.location,
                    onValueChange = { viewModel.setLocation(it) },
                    labelRes = R.string.location,
                    formHint = locationErr.ifBlank { stringResource(R.string.location_hint) },
                    isError = locationErr.isNotBlank(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                PostForm(
                    value = uiState.value.description,
                    onValueChange = { viewModel.setDescription(it) },
                    labelRes = R.string.description,
                    formHint = descriptionErr.ifBlank { stringResource(R.string.description_hint) },
                    isError = descriptionErr.isNotBlank(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    formHeight = 208.dp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                PostForm(
                    value = uiState.value.tags,
                    onValueChange = { viewModel.setTags(it) },
                    labelRes = R.string.tags,
                    formHint = tagsErr.ifBlank { stringResource(R.string.tags_hint) },
                    isError = tagsErr.isNotBlank(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun CreatePostPreview() {
    TemuanTelyuTheme {
        CreatePostScreen(rememberNavController())
    }
}