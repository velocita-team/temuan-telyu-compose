package id.my.ariqnf.temuantelyu.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.my.ariqnf.temuantelyu.data.Chat
import id.my.ariqnf.temuantelyu.data.Contact
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme
import id.my.ariqnf.temuantelyu.util.relativeTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactList(contact: Contact, onContactClick: () -> Unit, modifier: Modifier = Modifier ) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = onContactClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = contact.latestChat.timestamp.relativeTime().toString(),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            Text(
                text = contact.latestChat.message,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ContactListPreview() {
    TemuanTelyuTheme {
        ContactList(
            Contact(
                name = "Lorem ipsum dolor sit amet consectetur adipisicing elit",
                latestChat = Chat("Agung", "Lorem ipsum dolor sit amet consectetur adipisicing elit. At harum odit optio assumenda rerum ratione pariatur animi excepturi beatae sunt ullam laborum ab enim, corporis itaque sed dolores ipsam maxime.")
            ),
            {}
        )
    }
}