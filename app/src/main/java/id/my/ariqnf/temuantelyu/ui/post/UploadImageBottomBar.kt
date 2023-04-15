package id.my.ariqnf.temuantelyu.ui.post

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme

@Composable
fun UploadImgBottomBar(onUpload: () -> Unit, modifier: Modifier = Modifier, imageUri: Uri? = null) {
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.5.dp,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        IconButton(onClick = onUpload, modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (imageUri == null) {
                    Icon(painter = painterResource(R.drawable.image), contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.upload_photo),
                        style = MaterialTheme.typography.headlineMedium
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(imageUri).build()
                        ),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun UploadButtonPreview() {
    TemuanTelyuTheme {
        UploadImgBottomBar({})
    }
}