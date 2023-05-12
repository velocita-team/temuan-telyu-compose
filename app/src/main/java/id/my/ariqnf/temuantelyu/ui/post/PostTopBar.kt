package id.my.ariqnf.temuantelyu.ui.post

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostTopBar(navigateBack: () -> Unit, modifier: Modifier = Modifier, onPost: () -> Unit = {}, enablePost: Boolean = true) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.discard_post)
                )
            }
        },
        actions = {
            Button(onClick = onPost, enabled = enablePost) {
                Text(text = stringResource(R.string.post).uppercase())
            }
        },
        title = {},
        modifier = modifier,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Preview
@Composable
fun CreatePostTopBarPreview() {
    TemuanTelyuTheme {
        CreatePostTopBar({})
    }
}