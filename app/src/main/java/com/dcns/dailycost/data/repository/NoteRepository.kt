package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.remote.handlers.NoteHandler
import com.dcns.dailycost.data.model.remote.response.NoteResponse
import com.dcns.dailycost.data.model.remote.response.UploadImageResponse
import com.dcns.dailycost.domain.repository.INoteRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteHandler: NoteHandler
): INoteRepository {

    override suspend fun getNote(token: String): Response<NoteResponse> {
        return noteHandler.getNote(token)
    }

    override suspend fun addNote(token: String, body: RequestBody): Response<NoteResponse> {
        return noteHandler.addNote(token, body)
    }

    override suspend fun getNoteById(userId: Int, token: String): Response<NoteResponse> {
        return noteHandler.getNoteById(userId, token)
    }

    override suspend fun uploadImage(image: MultipartBody.Part): Response<UploadImageResponse> {
        return noteHandler.uploadImage(image)
    }

}