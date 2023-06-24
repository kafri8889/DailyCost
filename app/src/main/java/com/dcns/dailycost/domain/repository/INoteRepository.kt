package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.local.NoteDb
import com.dcns.dailycost.data.model.remote.response.NoteResponse
import com.dcns.dailycost.data.model.remote.response.UploadImageResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface INoteRepository {

    suspend fun getNoteRemote(
        token: String
    ): Response<NoteResponse>

    suspend fun addNoteRemote(
        token: String,
        body: RequestBody
    ): Response<NoteResponse>

    suspend fun getNoteByIdRemote(
        userId: Int,
        token: String
    ): Response<NoteResponse>

    suspend fun uploadImage(
        image: MultipartBody.Part
    ): Response<UploadImageResponse>

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