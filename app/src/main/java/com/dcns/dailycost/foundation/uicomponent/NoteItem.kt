package com.dcns.dailycost.foundation.uicomponent

import android.graphics.drawable.GradientDrawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dcns.dailycost.data.datasource.local.LocalNoteDataProvider
import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.foundation.extension.drawFadedEdge
import com.dcns.dailycost.theme.DailyCostTheme
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import timber.log.Timber
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

    val secondaryColor = MaterialTheme.colorScheme.secondary

    val context = LocalContext.current

    var isError by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }

    val date = remember(note) {
        DateFormat.getDateInstance(DateFormat.MEDIUM).format(note.date)
    }

    val imageRequest = remember(note.imageUrl) {
        ImageRequest.Builder(context)
            .data(note.imageUrl)
            .crossfade(true)
            .crossfade(1000)
            .error(
                GradientDrawable().apply {
                    setColor(secondaryColor.toArgb())
                }
            )
            .listener(
                onError = { _, e ->
                    Timber.e(e.throwable, "request failed: ${e.throwable.message}")

                    isError = true
                    isLoading = false
                },
                onStart = { _ ->
                    isError = false
                    isLoading = true
                },
                onSuccess = { _, _ ->
                    isError = false
                    isLoading = false
                }
            )
            .build()
    }

    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = note.imageUrl.isNotBlank(),
                enter = expandHorizontally(tween(512)),
                exit = shrinkHorizontally(tween(512)),
            ) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(96.dp)
                        .placeholder(
                            visible = isLoading,
                            color = MaterialTheme.colorScheme.outline,
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = Color.White
                            )
                        )
                )
            }

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
