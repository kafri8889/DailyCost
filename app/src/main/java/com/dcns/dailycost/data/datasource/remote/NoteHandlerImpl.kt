package com.dcns.dailycost.data.datasource.remote

import com.dcns.dailycost.data.datasource.remote.handlers.NoteHandler
import com.dcns.dailycost.data.datasource.remote.services.NoteService
import com.dcns.dailycost.data.model.remote.response.NoteResponse
import com.dcns.dailycost.data.model.remote.response.UploadImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class NoteHandlerImpl @Inject constructor(
    private val noteService: NoteService
): NoteHandler {

    override suspend fun getNote(token: String): Response<NoteResponse> {
        return noteService.getNote(token)
    }

    override suspend fun addNote(token: String, body: RequestBody): Response<NoteResponse> {
        return noteService.addNote(token, body)
    }

    override suspend fun getNoteById(userId: Int, token: String): Response<NoteResponse> {
        return noteService.getNoteById(userId, token)
    }

    override suspend fun uploadImage(image: MultipartBody.Part): Response<UploadImageResponse> {
        return noteService.uploadImage(image)
    }
}