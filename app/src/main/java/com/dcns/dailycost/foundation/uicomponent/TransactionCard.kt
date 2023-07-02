package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.data.datasource.local.LocalExpenseDataProvider
import com.dcns.dailycost.foundation.common.CommonDateFormatter
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.common.Transaction
import com.dcns.dailycost.foundation.extension.dailyCostMarquee
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCard(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp
                )
        ) {
            Icon(
                painter = ColorPainter(Color.LightGray),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = transaction.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = DailyCostTheme.colorScheme.text
                    ),
                    modifier = Modifier
                        .dailyCostMarquee(
                            edgeWidth = 0.dp
                        )
                )

                Text(
                    text = CommonDateFormatter.edmy.format(transaction.date),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal,
                        color = DailyCostTheme.colorScheme.labelText
                    )
                )
            }

            Text(
                text = transaction.parseAmount(LocalCurrency.current.countryCode),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DailyCostTheme.colorScheme.text
                ),
                modifier = Modifier
                    .weight(1f)
                    .dailyCostMarquee(
                        edgeWidth = 0.dp
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionCardPreview() {
    DailyCostTheme {
        TransactionCard(
            transaction = LocalExpenseDataProvider.expense1.copy(
                name = "Loooooooooooooooooonnnnnnnnnnnnnnnggggggggggg tttteeeeeeekkkkkk"
            ),
            onClick = {

            }
        )
    }
}
