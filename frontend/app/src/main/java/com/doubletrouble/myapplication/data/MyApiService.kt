package com.doubletrouble.myapplication.data

import com.doubletrouble.myapplication.data.model.Humidity
import com.doubletrouble.myapplication.data.model.Tree
import com.doubletrouble.myapplication.data.model.TreeHealth
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
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
        @Query("id") cameraId: String = "esp32_cam_1",
        @Query("manual") manual: Boolean = false,
    ): TreeHealth

    @GET("api/tree/today-hours")
    suspend fun getTodayLightHours() : Int

    @GET("api/tree/light-status")
    suspend fun getTodayLightStatus() : String

    @GET("api/tree/water-level")
    suspend fun getWaterLevel() : Int

    @GET("api/tree/humidity")
    suspend fun getHumidity() : Int

    @GET("api/tree/humidity/history")
    suspend fun getHumidityHistory(): List<Humidity>

    @POST("api/tree/light-status")
    suspend fun postLightStatus(
        @Query("id") cameraId: String = "esp32_cam_1",
        @Query("light-status") lightStatus : String = "ON"
    ) :  String

    @POST("/api/tree/water")
    suspend fun triggerWaterPump(): Response<Unit>
}