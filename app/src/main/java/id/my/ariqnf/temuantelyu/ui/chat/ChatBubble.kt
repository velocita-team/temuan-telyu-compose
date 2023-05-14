package id.my.ariqnf.temuantelyu.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.my.ariqnf.temuantelyu.data.Chat
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.util.formatDate

@Composable
fun ChatBubble(
    chat: Chat,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape: Shape = CardDefaults.shape
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = horizontalAlignment) {
        Card(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .sizeIn(maxWidth = 320.dp),
            colors = colors,
            shape = shape
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp), verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = chat.message,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = chat.timestamp.formatDate("HH:mm"),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatBubblePreview() {
    TemuanTelyuTheme {
        ChatBubble(Chat(message = "Leon sedang mencari seorang anak presiden yang konon diculik."))
    }
}