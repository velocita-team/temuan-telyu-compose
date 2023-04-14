package id.my.ariqnf.temuantelyu.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Timestamp
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.ui.theme.Green600
import id.my.ariqnf.temuantelyu.ui.theme.Red600
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.util.relativeTime

@Composable
fun PostCard(
    post: Post,
    modifier: Modifier = Modifier,
    onComment: () -> Unit = {},
    onShare: () -> Unit = {}
) {
    val cateColor = if (post.cate == "lost") Red600 else Green600
    val formattedDate = post.date?.relativeTime() ?: ""

    Card(
        modifier = modifier,
        border = BorderStroke(1.dp, Color.Black.copy(0.12f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "${post.title}", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "${post.sender} | $formattedDate",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(cateColor)
                )
            }
            if (!post.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(post.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Text(
                text = "${post.content}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onComment) {
                    Icon(
                        painter = painterResource(R.drawable.sms),
                        contentDescription = stringResource(R.string.comment)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(R.string.share)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PostCardPreview() {
    TemuanTelyuTheme(darkTheme = true) {
        PostCard(post = Post(
            title = "Dompet ilang",
            sender = "Thomas",
            date = Timestamp.now(),
            content = "Dompet saya hilang kemarin",
            cate = "lost",
        ))
    }
}