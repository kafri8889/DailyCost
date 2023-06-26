package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.remote.response.AddNoteResponse
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.EditNoteResponse
import com.dcns.dailycost.data.model.remote.response.GetNoteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface NoteService {

    @POST("/api/catatan")
    @Multipart
    suspend fun addNote(
//        @Header("accept") accept: String = "application/json",
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part("date") date: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part img: MultipartBody.Part
    ): Response<AddNoteResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("/api/catatan")
    suspend fun editNote(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Response<EditNoteResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @HTTP(method = "DELETE", path = "/api/catatan", hasBody = true)
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Response<DeleteResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/catatan/{id}")
    suspend fun getNoteById(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Response<GetNoteResponse>

}