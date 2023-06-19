package com.dcns.dailycost.data.datasource.local

import com.dcns.dailycost.data.model.Note

object LocalNoteDataProvider {

    val note1 = Note(
        id = "0",
        title = "Lorem ipsum",
        body = "Loerem Ipsum dolor sit amet, consectetur adipiscing elit sed diam nonum nib tempor",
        date = System.currentTimeMillis(),
        imageUrl = "https://github.com/kafri8889/resources/blob/main/wonyoung3.jpg",
        userId = "73"
    )

    val note2 = Note(
        id = "1",
        title = "Loerem Ipsum dolor sit amet, consectetur adipiscing elit sed diam nonum nib tempor",
        body = "Loerem Ipsum dolor sit amet, consectetur adipiscing elit sed diam nonum nib tempor dolore magna aliqu Lorem ipsum dolor sit amet, consectetur adip iscing elit sed diam nonum nib tempor dolore magna al Vivamus consequ tempor",
        date = System.currentTimeMillis(),
        imageUrl = "https://github.com/kafri8889/resources/blob/main/yunjin1.jpg",
        userId = "73"
    )

    val notes = arrayOf(
        note1,
        note2
    )

}