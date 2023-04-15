package id.my.ariqnf.temuantelyu.ui.post

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.ui.theme.Green600
import id.my.ariqnf.temuantelyu.ui.theme.Red600
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.util.formatDate

@Composable
fun MyPostCard(
    post: Post,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val (colorCategory, btnLabel) = when (post.cate) {
        "lost" -> listOf(Red600, R.string.found)
        else -> listOf(Green600, R.string.returned)
    }
    val formattedDate = post.date?.formatDate() ?: ""

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!post.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(post.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = post.title,
                    modifier = Modifier
                        .sizeIn(maxWidth = 100.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(if (post.imageUrl != null) 16.dp else 4.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = post.title!!, style = MaterialTheme.typography.headlineMedium)
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(colorCategory as Color)
                    )
                }
                Button(
                    onClick = onClick, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(text = stringResource(btnLabel as Int))
                }
            }
        }
    }
}

@Preview
@Composable
fun MyPostCardPreview() {
    TemuanTelyuTheme {
        MyPostCard(
            post = Post(title = "Hp oppo", sender = "me", content = "test", cate = "found")
        )
    }
}