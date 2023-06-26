package com.dcns.dailycost.domain.use_case.note

import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.data.model.remote.response.AddNoteResponse
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.foundation.extension.toNoteDb
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


class AddRemoteNoteUseCase(
    private val noteRepository: INoteRepository
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * @param image file imagenya, usahain dari cache dan sudah di compress
     */
    suspend operator fun invoke(
        userId: String,
        token: String,
        note: Note,
        image: File
    ): Response<AddNoteResponse> {
        noteRepository.upsertNote(note.toNoteDb())

        val mTitle = note.title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val mBody = note.body.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val mDate = dateFormat.format(note.createdAt).toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val mUserId = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val reqFile = image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val mFile = MultipartBody.Part.createFormData(
            "file",
            image.name,
            reqFile
        )

        return noteRepository.addNote(
            token = token,
            title = mTitle,
            body = mBody,
            date = mDate,
            userId = mUserId,
            file = mFile
        )
    }

}