package id.my.ariqnf.temuantelyu.ui.profile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.my.ariqnf.temuantelyu.R
import id.my.ariqnf.temuantelyu.ui.theme.TemuanTelyuTheme

@Composable
fun ProfileMenu(
    @DrawableRes iconRes: Int,
    @StringRes labelRes: Int,
    modifier: Modifier = Modifier,
    borderWidth: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.onSurface,
    @DrawableRes trailingIconRes: Int? = null
) {
    CompositionLocalProvider(
        LocalContentColor provides color
    ) {
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .border(
                    BorderStroke(
                        borderWidth,
                        MaterialTheme.colorScheme.surfaceVariant
                    ), RoundedCornerShape(4.dp)
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Icon(painter = painterResource(iconRes), contentDescription = null)
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 24.dp)
            )
            if (trailingIconRes != null) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = painterResource(trailingIconRes), contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
private fun ProfileMenuPreview() {
    TemuanTelyuTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            ProfileMenu(
                iconRes = R.drawable.article,
                labelRes = R.string.my_posts,
                trailingIconRes = R.drawable.arrow_forward_ios,
            )
        }
    }
}