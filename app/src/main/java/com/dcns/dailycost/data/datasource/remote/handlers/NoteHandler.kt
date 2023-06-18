package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.NoteResponse
import com.dcns.dailycost.data.model.remote.response.UploadImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface NoteHandler {

    suspend fun getNote(
        token: String
    ): Response<NoteResponse>

    suspend fun addNote(
        token: String,
        body: RequestBody
    ): Response<NoteResponse>

    suspend fun getNoteById(
        userId: Int,
        token: String
    ): Response<NoteResponse>

    suspend fun uploadImage(
        image: MultipartBody.Part
    ): Response<UploadImageResponse>

}