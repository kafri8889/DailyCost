package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.AddNoteResponse
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.EditNoteResponse
import com.dcns.dailycost.data.model.remote.response.GetNoteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface NoteHandler {

    /**
     * Tambah catatan baru
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
     * Edit catatan
     */
    suspend fun editNote(
        token: String,
        body: RequestBody
    ): Response<EditNoteResponse>

    /**
     * Hapus catatan
     */
    suspend fun deleteNote(
        token: String,
        body: RequestBody
    ): Response<DeleteResponse>

    /**
     * Dapatkan catatan by id
     */
    suspend fun getNoteById(
        userId: Int,
        token: String
    ): Response<GetNoteResponse>

}