package com.dcns.dailycost

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dcns.dailycost.data.datasource.remote.services.NoteService
import com.dcns.dailycost.foundation.util.TestUtil
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


@RunWith(AndroidJUnit4::class)
class NoteServiceTest {

    private lateinit var noteService: NoteService
    private lateinit var context: Context

    @Before
    fun setUp() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        context = ApplicationProvider.getApplicationContext()
        noteService = retrofit.create(NoteService::class.java)
    }

    @Test
    fun addNote() = runTest {

       val ins = context.assets.open("pharita.png")

        val tempFile = File.createTempFile("pharita", ".png")

        assert(tempFile.exists()) { "File not exists" }

        tempFile.outputStream().use {
            BitmapFactory
                .decodeStream(ins)
                .compress(Bitmap.CompressFormat.PNG, 30, it)
        }

        val reqFile = tempFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartFile = MultipartBody.Part.createFormData(
            "file",
            tempFile.name,
            reqFile
        )

        val title = "pritaaaaaaaaaaaa".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = "prita cewe quuu".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val date = "2023-07-07".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val userId = TestUtil.adminUserId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        noteService.addNote(
            token = TestUtil.adminToken,
            title = title,
            body = body,
            date = date,
            userId = userId,
            img = multipartFile
        ).let { response ->
            TestUtil.printResponse(response)

            assert(response.isSuccessful) { "response failed | ${TestUtil.getPrintedResponse(response)}" }
            assert(response.body() != null) { "response body null" }
        }
    }

}