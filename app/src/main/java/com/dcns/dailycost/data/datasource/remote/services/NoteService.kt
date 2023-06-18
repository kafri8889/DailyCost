package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.networking.response.NoteResponse
import com.dcns.dailycost.data.model.networking.response.UploadImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface NoteService {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/catatan")
    suspend fun getNote(
        @Header("Authorization") token: String
    ): Response<NoteResponse>

    // https://stackoverflow.com/a/52620905/15967135
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/catatan")
    suspend fun addNote(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Response<NoteResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/catatan/{id}")
    suspend fun getNoteById(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Response<NoteResponse>

    @Multipart
    @POST("/api/upload/image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<UploadImageResponse>

}