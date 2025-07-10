package com.dojagy.todaysave.data.source.api.service

import com.dojagy.todaysave.data.dto.MainDto
import com.dojagy.todaysave.data.dto.content.ContentDto
import com.dojagy.todaysave.data.dto.content.ContentSaveDto
import com.dojagy.todaysave.data.dto.content.MetadataDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ContentService {

    @POST("api/content")
    suspend fun saveContent(
        @Body body: ContentSaveDto
    ): Response<MainDto<ContentDto>>

    @GET("api/content/metadata")
    suspend fun metadata(
        @Query("url") url: String
    ): Response<MainDto<MetadataDto>>
}