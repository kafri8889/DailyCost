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


class AddNoteUseCase(
    private val noteRepository: INoteRepository
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Kalo buat upload ke server, token sama imagenya ga boleh null
     */
    suspend operator fun invoke(
        image: File? = null,
        token: String? = null,
        note: Note
    ): Response<NoteResponse> {
        noteRepository.upsertNote(note.toNoteDb())

        return if (image != null && token != null) {
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
                addFormDataPart("date", dateFormat.format(note.date))
                addFormDataPart("title", note.title)
                addFormDataPart("user_id", note.userId)
            }

            noteRepository.addNoteRemote(
                token = token,
                body = builder.build()
            )
        } else Response.error(
            403,
            "Token atau image null".toResponseBody(null)
        )
    }

}