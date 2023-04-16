package id.my.ariqnf.temuantelyu.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme

@Composable
fun LostFoundButton(
    modifier: Modifier = Modifier,
    onLostClick: () -> Unit = {},
    onFoundClick: () -> Unit = {},
    enableLost: Boolean = true,
    enableFound: Boolean = true,
    lostChecked: Boolean = true,
    foundChecked: Boolean = true,
    role: Role = Role.RadioButton
) {
    Row(modifier = modifier) {
        Box(
//            ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.surfaceVariant,
//                contentColor = MaterialTheme.colorScheme.onSurface,
//                disabledContainerColor = MaterialTheme.colorScheme.primary,
//                disabledContentColor = MaterialTheme.colorScheme.onPrimary
//            ),
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (lostChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
                .weight(1f)
                .height(32.dp)
                .padding(horizontal = 24.dp)
                .clickable(enableLost, role = role, onClick = onLostClick)
        ) {
            Text(
                text = stringResource(R.string.lost),
                color = if (lostChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Box(
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.surfaceVariant,
//                contentColor = MaterialTheme.colorScheme.onSurface,
//                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
//                disabledContentColor = MaterialTheme.colorScheme.onTertiary
//            ),
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (foundChecked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant
                )
                .weight(1f)
                .height(32.dp)
                .padding(horizontal = 24.dp)
                .clickable(enableFound, role = role, onClick = onFoundClick)
        ) {
            Text(
                text = stringResource(R.string.found),
                color = if (foundChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
fun LostFoundButtonPreview() {
    TemuanTelyuTheme {
        LostFoundButton()
    }
}