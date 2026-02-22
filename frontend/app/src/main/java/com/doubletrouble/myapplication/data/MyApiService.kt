package com.doubletrouble.myapplication.data

import com.doubletrouble.myapplication.data.model.Tree
import com.doubletrouble.myapplication.data.model.TreeHealth
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApiService {
    @GET("api/tree/generate-fact")
    suspend fun getFact() : String

    @GET("api/tree/get-tree")
    suspend fun getTree(
        @Query("id") cameraId: String = "esp32_cam_1"
    ): Tree

    @GET("api/tree/analyze-health")
    suspend fun getTreeHealth(
        @Query("id") cameraId: String = "esp32_cam_1"
    ): TreeHealth
}