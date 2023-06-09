package id.my.ariqnf.temuantelyu.ui.post

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostForm(
    @StringRes labelRes: Int,
    formHint: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    formHeight: Dp = 56.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    value: String = "",
    isError: Boolean = false,
    onValueChange: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        TextField(
            label = {
                Text(text = stringResource(labelRes))
            },
            value = value,
            isError = isError,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(formHeight),
            singleLine = singleLine,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Text(
            text = formHint,
            modifier = Modifier.padding(start = 16.dp, top = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PostInputPreview() {
    TemuanTelyuTheme(darkTheme = false) {
        PostForm(labelRes = R.string.title, formHint = stringResource(R.string.title))
    }
}