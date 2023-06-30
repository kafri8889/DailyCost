package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.R
import com.dcns.dailycost.theme.DailyCostTheme

@Preview(showBackground = true)
@Composable
private fun DrawerItemPreview() {
    DailyCostTheme {
        DrawerItem(
            summary = {
                Text("summary")
            },
            title = {
                Text("title")
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun DrawerItem(
    modifier: Modifier = Modifier,
    summary: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit
) {

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
        ) {
            icon()

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ProvideTextStyle(
                    content = title,
                    value = MaterialTheme.typography.bodyLarge.copy(
                        color = DailyCostTheme.colorScheme.text,
                        fontWeight = FontWeight.Medium
                    )
                )

                ProvideTextStyle(
                    content = summary,
                    value = MaterialTheme.typography.bodyMedium.copy(
                        color = DailyCostTheme.colorScheme.labelText,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}
