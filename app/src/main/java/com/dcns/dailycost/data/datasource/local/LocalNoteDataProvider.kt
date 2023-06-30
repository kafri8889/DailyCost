package com.dcns.dailycost.data.datasource.local

import com.dcns.dailycost.data.model.Note

/**
 * Data provider lokal untuk note (berguna untuk testing)
 */
object LocalNoteDataProvider {

    val note1 = Note(
        id = "0",
        title = "Lorem ipsum",
        body = "Loerem Ipsum dolor sit amet, consectetur adipiscing elit sed diam nonum nib tempor",
        createdAt = System.currentTimeMillis(),
        imageUrl = "https://raw.githubusercontent.com/kafri8889/resources/main/wonyoung3.jpg",
        userId = "73"
    )

    val note2 = Note(
        id = "1",
        title = "Loerem Ipsum dolor sit amet, consectetur adipiscing elit sed diam nonum nib tempor",
        body = "Loerem Ipsum dolor sit amet, consectetur adipiscing elit sed diam nonum nib tempor dolore magna aliqu Lorem ipsum dolor sit amet, consectetur adip iscing elit sed diam nonum nib tempor dolore magna al Vivamus consequ tempor",
        createdAt = System.currentTimeMillis(),
        imageUrl = "https://raw.githubusercontent.com/kafri8889/resources/main/yunjin1.jpeg",
        userId = "73"
    )

    val notes = arrayOf(
        note1,
        note2
    )

}