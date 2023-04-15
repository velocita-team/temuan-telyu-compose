package id.my.ariqnf.temuantelyu.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme

@Composable
fun ProfileBanner(name: String, email: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant), MaterialTheme.shapes.small)
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp),
        ) {
            Text(text = name, style = MaterialTheme.typography.headlineLarge)
            Text(text = email, style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Preview
@Composable
fun ProfileBannerPreview() {
    TemuanTelyuTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileBanner(name = "Adam smith", email = "adamsmith@mail.com")
        }
    }
}