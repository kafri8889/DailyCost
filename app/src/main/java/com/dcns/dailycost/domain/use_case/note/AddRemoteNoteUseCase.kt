package com.dcns.dailycost.domain.use_case.note

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.data.model.remote.response.NoteResponse
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.foundation.extension.toNoteDb
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


class AddRemoteNoteUseCase(
    private val noteRepository: INoteRepository
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    suspend operator fun invoke(
        image: File,
        token: String,
        note: Note
    ): Response<NoteResponse> {
        noteRepository.upsertNote(note.toNoteDb())

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (image.exists()) {
            val bmp = BitmapFactory.decodeFile(image.absolutePath)
            val bos = ByteArrayOutputStream()

            bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos)

            builder.addFormDataPart(
                "file",
                image.nameWithoutExtension,
                bos.toByteArray().toRequestBody(MultipartBody.FORM)
            )
        } else {
            return Response.error(
                400,
                "File image belum di buat".toResponseBody(null)
            )
        }

        with(builder) {
            addFormDataPart("body", note.body)
            addFormDataPart("date", dateFormat.format(note.createdAt))
            addFormDataPart("title", note.title)
            addFormDataPart("user_id", note.userId)
        }

        return noteRepository.addNoteRemote(
            token = token,
            body = builder.build()
        )
    }

}