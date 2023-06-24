package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.local.dao.NoteDao
import com.dcns.dailycost.data.datasource.remote.handlers.NoteHandler
import com.dcns.dailycost.data.model.local.NoteDb
import com.dcns.dailycost.data.model.remote.response.NoteResponse
import com.dcns.dailycost.data.model.remote.response.UploadImageResponse
import com.dcns.dailycost.domain.repository.INoteRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteHandler: NoteHandler,
    private val noteDao: NoteDao
): INoteRepository {

    override suspend fun getNoteRemote(token: String): Response<NoteResponse> {
        return noteHandler.getNote(token)
    }

    override suspend fun addNoteRemote(token: String, body: RequestBody): Response<NoteResponse> {
        return noteHandler.addNote(token, body)
    }

    override suspend fun getNoteByIdRemote(userId: Int, token: String): Response<NoteResponse> {
        return noteHandler.getNoteById(userId, token)
    }

    override suspend fun uploadImage(image: MultipartBody.Part): Response<UploadImageResponse> {
        return noteHandler.uploadImage(image)
    }

    override suspend fun updateNote(vararg note: NoteDb) {
        noteDao.updateNote(*note)
    }

    override suspend fun upsertNote(vararg note: NoteDb) {
        noteDao.upsertNote(*note)
    }

    override suspend fun deleteNote(vararg note: NoteDb) {
        noteDao.deleteNote(*note)
    }

    override suspend fun insertNote(vararg note: NoteDb) {
        noteDao.insertNote(*note)
    }

    override suspend fun deleteNoteExcept(ids: List<String>) {
        noteDao.deleteNoteExcept(ids)
    }

    override fun getAllNoteLocal(): Flow<List<NoteDb>> {
        return noteDao.getAllNote()
    }

    override fun getNoteByIdLocal(id: Int): Flow<NoteDb?> {
        return noteDao.getNoteById(id)
    }

    override fun getNoteByUserIdLocal(id: Int): Flow<List<NoteDb>> {
        return noteDao.getNoteByUserId(id)
    }
}