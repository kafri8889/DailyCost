package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dcns.dailycost.data.datasource.local.LocalNoteDataProvider
import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.foundation.extension.drawFadedEdge
import com.dcns.dailycost.theme.DailyCostTheme
import java.text.DateFormat

@Preview(device = "spec:width=360dp,height=700dp,dpi=440")
@Composable
private fun NoteItemPreview() {
    DailyCostTheme {
        NoteItem(
            note = LocalNoteDataProvider.note2,
            onClick = {

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val date = remember(note) {
        DateFormat.getDateInstance(DateFormat.MEDIUM).format(note.date)
    }

    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // TODO: Kasih efek loading, ambil dari project bookman
            AsyncImage(
                model = note.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleSmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            // Rendering to an offscreen buffer is required to get the faded edges' alpha to be
                            // applied only to the text, and not whatever is drawn below this composable (e.g. the
                            // window).
                            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                            .drawWithContent {
                                drawContent()
                                drawFadedEdge(
                                    edgeWidth = 8.dp,
                                    leftEdge = false
                                )
                            }
                            .basicMarquee(
                                delayMillis = 2000
                            )
                    )

                    // Date
                    Text(
                        text = date,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )
                }

                // Body
                Text(
                    text = note.body,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }
        }
    }
}
