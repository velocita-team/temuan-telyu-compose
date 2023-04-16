package id.my.ariqnf.temuantelyu.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.util.rememberImeState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    userName: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = {},
    onClear: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val imeState = rememberImeState()
    var isSearch by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = imeState.value) {
        if (!imeState.value) {
            isSearch = false
        }
    }

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            IconButton(onClick = {
                if (isSearch && value.isNotBlank()) {
                    onSearch()
                }
                isSearch = true
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                )
            }
        },
        title = {
            AnimatedVisibility(
                visible = !isSearch,
                enter = slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 150,
                        delayMillis = 200
                    )
                ),
                exit = slideOutHorizontally(animationSpec = tween(durationMillis = 150))
            ) {
                Text(
                    text = stringResource(R.string.greeting, userName),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            AnimatedVisibility(
                visible = isSearch,
                enter = slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 150,
                        delayMillis = 200
                    )
                ),
                exit = slideOutHorizontally(animationSpec = tween(durationMillis = 150))
            ) {
                // Request focus when search bar is visible
                LaunchedEffect(Unit) {
                    delay(200)
                    focusRequester.requestFocus()
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                isSearch = false
                                onSearch()
                            }
                        ),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .matchParentSize()
                            .clip(
                                MaterialTheme.shapes.medium
                            )
                            .background(MaterialTheme.colorScheme.surface)
                            .focusRequester(focusRequester), // add focusRequester modifier to request changes
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .padding(start = 6.dp)
                            ) {
                                innerTextField()
                                IconButton(onClick = {
                                    isSearch = false
                                    onClear()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = stringResource(R.string.clear_text),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun HomeTopBarPreview() {
    TemuanTelyuTheme {
        HomeTopBar(userName = "Telyutizen", value = "", onValueChange = {})
    }
}