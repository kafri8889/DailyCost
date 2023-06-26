package com.dcns.dailycost.data.datasource.remote

import com.dcns.dailycost.data.datasource.remote.handlers.NoteHandler
import com.dcns.dailycost.data.datasource.remote.services.NoteService
import com.dcns.dailycost.data.model.remote.response.AddNoteResponse
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.EditNoteResponse
import com.dcns.dailycost.data.model.remote.response.GetNoteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class NoteHandlerImpl @Inject constructor(
    private val noteService: NoteService
): NoteHandler {
    override suspend fun addNote(
        token: String,
        title: RequestBody,
        body: RequestBody,
        date: RequestBody,
        userId: RequestBody,
        file: MultipartBody.Part
    ): Response<AddNoteResponse> {
        return noteService.addNote(token, title, body, date, userId, file)
    }


    override suspend fun editNote(token: String, body: RequestBody): Response<EditNoteResponse> {
        return noteService.editNote(token, body)
    }

    override suspend fun deleteNote(token: String, body: RequestBody): Response<DeleteResponse> {
        return noteService.deleteNote(token, body)
    }

    override suspend fun getNoteById(userId: Int, token: String): Response<GetNoteResponse> {
        return noteService.getNoteById(userId, token)
    }


}