package com.dcns.dailycost.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.dcns.dailycost.data.model.local.NoteDb
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    fun getAllNote(): Flow<List<NoteDb>>

    @Query("SELECT * FROM note_table WHERE id_note LIKE :id")
    fun getNoteById(id: Int): Flow<NoteDb?>

    @Query("SELECT * FROM note_table WHERE userId_note LIKE :id")
    fun getNoteByUserId(id: Int): Flow<List<NoteDb>>

    @Query("DELETE FROM note_table WHERE id_note NOT IN (:notes)")
    fun deleteExcept(notes: List<NoteDb>)

    @Update
    fun updateNote(vararg note: NoteDb)

    @Upsert
    fun upsertNote(vararg note: NoteDb)

    @Delete
    fun deleteNote(vararg note: NoteDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(vararg note: NoteDb)

}