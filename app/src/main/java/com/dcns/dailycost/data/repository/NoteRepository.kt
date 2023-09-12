package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.local.dao.NoteDao
import com.dcns.dailycost.data.datasource.remote.handlers.NoteHandler
import com.dcns.dailycost.data.model.local.NoteDb
import com.dcns.dailycost.data.model.remote.response.AddNoteResponse
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.EditNoteResponse
import com.dcns.dailycost.data.model.remote.response.GetNoteResponse
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
	override suspend fun addNote(
		token: String,
		title: RequestBody,
		body: RequestBody,
		date: RequestBody,
		userId: RequestBody,
		file: MultipartBody.Part
	): Response<AddNoteResponse> {
		return noteHandler.addNote(token, title, body, date, userId, file)
	}

	override suspend fun editNoteRemote(
		token: String,
		body: RequestBody
	): Response<EditNoteResponse> {
		return noteHandler.editNote(token, body)
	}

	override suspend fun deleteNoteRemote(
		token: String,
		body: RequestBody
	): Response<DeleteResponse> {
		return noteHandler.deleteNote(token, body)
	}

	override suspend fun getNoteByIdRemote(userId: Int, token: String): Response<GetNoteResponse> {
		return noteHandler.getNoteById(userId, token)
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

	override fun getAllLocalNote(): Flow<List<NoteDb>> {
		return noteDao.getAllNote()
	}

	override fun getLocalNoteById(id: Int): Flow<NoteDb?> {
		return noteDao.getNoteById(id)
	}

	override fun getLocalNoteByUserId(id: Int): Flow<List<NoteDb>> {
		return noteDao.getNoteByUserId(id)
	}
}