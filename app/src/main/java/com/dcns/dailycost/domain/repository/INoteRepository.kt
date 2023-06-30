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

    /**
     * Tambah catatan baru ke database server
     */
    suspend fun addNote(
        token: String,
        title: RequestBody,
        body: RequestBody,
        date: RequestBody,
        userId: RequestBody,
        file: MultipartBody.Part,
    ): Response<AddNoteResponse>

    /**
     * Edit catatan ke database server
     */
    suspend fun editNoteRemote(
        token: String,
        body: RequestBody
    ): Response<EditNoteResponse>

    /**
     * Hapus catatan dari database server
     */
    suspend fun deleteNoteRemote(
        token: String,
        body: RequestBody
    ): Response<DeleteResponse>

    /**
     * Dapatkan catatan by id dari database server
     */
    suspend fun getNoteByIdRemote(
        userId: Int,
        token: String
    ): Response<GetNoteResponse>

    /**
     * Update catatan ke database lokal
     */
    suspend fun updateNote(vararg note: NoteDb)

    /**
     * Update atau insert catatan ke database lokal
     */
    suspend fun upsertNote(vararg note: NoteDb)

    /**
     * Hapus catatan dari database lokal
     */
    suspend fun deleteNote(vararg note: NoteDb)

    /**
     * Insert catatan ke database lokal
     */
    suspend fun insertNote(vararg note: NoteDb)

    /**
     * Hapus semua catatan dari database lokal, kecuali id yang diberikan
     * @param ids id yang di kecualikan
     */
    suspend fun deleteNoteExcept(ids: List<String>)

    /**
     * Dapatkan semua catatan dari database lokal
     */
    fun getAllNoteLocal(): Flow<List<NoteDb>>

    /**
     * Dapatkan catatan by id dari database lokal
     */
    fun getNoteByIdLocal(id: Int): Flow<NoteDb?>

    /**
     * Dapatkan catatan by user id dari database lokal
     */
    fun getNoteByUserIdLocal(id: Int): Flow<List<NoteDb>>

}