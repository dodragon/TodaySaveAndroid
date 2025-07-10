package com.dojagy.todaysave.data.domain.usecase

import com.dojagy.todaysave.data.domain.Return
import com.dojagy.todaysave.data.domain.repository.ContentApiRepository
import com.dojagy.todaysave.data.model.content.ContentModel
import com.dojagy.todaysave.data.model.content.MetadataModel
import javax.inject.Inject

class ContentUseCase @Inject constructor(
    private val apiRepo: ContentApiRepository
) : BaseUseCase() {

    suspend fun metadata(
        url: String
    ): Return<MetadataModel> {
        return apiRepo.metadata(url).oneTimeResult()
    }

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
    ): Return<ContentModel> {
        return apiRepo.saveContent(
            title = title,
            description = description,
            sharedUrl = sharedUrl,
            canonicalUrl = canonicalUrl,
            memo = memo,
            thumbnailUrl = thumbnailUrl,
            folderId = folderId,
            categoryId = categoryId,
            tags = tags
        ).oneTimeResult()
    }
}