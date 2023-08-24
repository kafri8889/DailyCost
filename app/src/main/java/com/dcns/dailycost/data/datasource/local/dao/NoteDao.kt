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

	@Query("DELETE FROM note_table WHERE id_note NOT IN (:ids)")
	suspend fun deleteNoteExcept(ids: List<String>)

	@Update
	suspend fun updateNote(vararg note: NoteDb)

	@Upsert
	suspend fun upsertNote(vararg note: NoteDb)

	@Delete
	suspend fun deleteNote(vararg note: NoteDb)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertNote(vararg note: NoteDb)

}