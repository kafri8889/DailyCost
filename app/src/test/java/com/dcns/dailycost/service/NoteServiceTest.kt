package com.dcns.dailycost.service

import com.dcns.dailycost.BuildConfig
import com.dcns.dailycost.data.datasource.remote.services.NoteService
import com.dcns.dailycost.data.model.remote.request_body.note.DeleteNoteRequestBody
import com.dcns.dailycost.data.model.remote.request_body.note.EditNoteRequestBody
import com.dcns.dailycost.foundation.util.TestUtil
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//@RunWith(RobolectricTestRunner::class)
//@Config(sdk = [28], resourceDir = "resources")
class NoteServiceTest {

    private lateinit var noteService: NoteService

    @BeforeEach
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        noteService = retrofit.create(NoteService::class.java)
    }

    @Test
    fun `get note by id`() = runTest {
        noteService.getNoteById(TestUtil.adminUserId, TestUtil.adminToken).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

    @Test
    fun `edit note`() = runTest {

        // Kalo mau test edit note
        // Pastiin [noteId] nya ada di server!
        val reqBody = EditNoteRequestBody(
            body = "new bodi",
            title = "nyu titel",
            userId = TestUtil.adminUserId.toString(),
            noteId = "41"
        ).toRequestBody()

        noteService.editNote(TestUtil.adminToken, reqBody).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

    @Test
    fun `delete note`() = runTest {

        // Kalo mau test delete note
        // Pastiin [noteId] nya ada di server!
        val reqBody = DeleteNoteRequestBody(
            userId = TestUtil.adminUserId.toString(),
            noteId = "41"
        ).toRequestBody()

        noteService.deleteNote(TestUtil.adminToken, reqBody).let { response ->
            TestUtil.printResponse(response)

            Truth.assertThat(response.isSuccessful).isTrue()
            Truth.assertThat(response.body()).isNotNull()
        }
    }

}