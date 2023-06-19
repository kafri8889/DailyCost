package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.data.datasource.local.LocalNoteDataProvider
import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.theme.DailyCostTheme

@Preview
@Composable
private fun NoteItemPreview() {
    DailyCostTheme {
        NoteItem(
            note = LocalNoteDataProvider.note1,
            onClick = {

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = note.title)
        }
    }
}
