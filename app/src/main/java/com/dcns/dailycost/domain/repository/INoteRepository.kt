package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.local.NoteDb
import com.dcns.dailycost.data.model.remote.response.AddNoteResponse
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.EditNoteResponse
import com.dcns.dailycost.data.model.remote.response.GetNoteResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface INoteRepository {

    suspend fun addNote(
        token: String,
        title: RequestBody,
        body: RequestBody,
        date: RequestBody,
        userId: RequestBody,
        file: MultipartBody.Part,
    ): Response<AddNoteResponse>

    suspend fun editNoteRemote(
        token: String,
        body: RequestBody
    ): Response<EditNoteResponse>

    suspend fun deleteNoteRemote(
        token: String,
        body: RequestBody
    ): Response<DeleteResponse>

    suspend fun getNoteByIdRemote(
        userId: Int,
        token: String
    ): Response<GetNoteResponse>

    suspend fun updateNote(vararg note: NoteDb)

    suspend fun upsertNote(vararg note: NoteDb)

    suspend fun deleteNote(vararg note: NoteDb)

    suspend fun insertNote(vararg note: NoteDb)

    /**
     * Delete all note from the database except the given [ids]
     */
    suspend fun deleteNoteExcept(ids: List<String>)

    fun getAllNoteLocal(): Flow<List<NoteDb>>

    fun getNoteByIdLocal(id: Int): Flow<NoteDb?>

    fun getNoteByUserIdLocal(id: Int): Flow<List<NoteDb>>

}