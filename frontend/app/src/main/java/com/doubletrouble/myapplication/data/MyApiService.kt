package com.doubletrouble.myapplication.data

import retrofit2.http.GET

interface MyApiService {
    @GET("api/tree/generate-fact")
    suspend fun getFact() : String
}