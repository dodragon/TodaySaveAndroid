package com.dojagy.todaysave.data.domain.repository

import com.dojagy.todaysave.data.model.content.ContentModel
import com.dojagy.todaysave.data.model.MainModel
import com.dojagy.todaysave.data.model.content.MetadataModel
import kotlinx.coroutines.flow.Flow

interface ContentApiRepository {

    suspend fun saveContent(
        title: String,
        description: String,
        sharedUrl: String,
        canonicalUrl: String,
        memo: String,
        thumbnailUrl: String,
        folderId: Long,
        categoryId: Long?,
        tags: List<String>
    ): Flow<MainModel<ContentModel>>

    suspend fun metadata(
        url: String
    ): Flow<MainModel<MetadataModel>>
}