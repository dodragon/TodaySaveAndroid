package com.dojagy.todaysave.data.source.api.repo

import com.dojagy.todaysave.data.domain.repository.ContentApiRepository
import com.dojagy.todaysave.data.dto.content.ContentSaveDto
import com.dojagy.todaysave.data.model.MainModel
import com.dojagy.todaysave.data.model.content.ContentModel
import com.dojagy.todaysave.data.model.content.MetadataModel
import com.dojagy.todaysave.data.model.mapper
import com.dojagy.todaysave.data.source.api.service.ContentService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContentApiRepositoryImpl @Inject constructor(
    private val contentService: ContentService
) : BaseApiRepository(), ContentApiRepository {

    override suspend fun saveContent(
        title: String,
        description: String,
        sharedUrl: String,
        canonicalUrl: String,
        memo: String,
        thumbnailUrl: String,
        folderId: Long,
        categoryId: Long?,
        tags: List<String>
    ): Flow<MainModel<ContentModel>> {
        return responseToModel(
            response = {
                contentService.saveContent(ContentSaveDto(
                    title = title,
                    description = description,
                    sharedUrl = sharedUrl,
                    canonicalUrl = canonicalUrl,
                    memo = memo,
                    thumbnailUrl = thumbnailUrl,
                    folderId = folderId,
                    categoryId = categoryId,
                    tags = tags
                ))
            },
            mapper = {
                it.mapper()
            }
        )
    }

    override suspend fun metadata(
        url: String
    ): Flow<MainModel<MetadataModel>> {
        return responseToModel(
            response = {
                contentService.metadata(url)
            },
            mapper = {
                it.mapper()
            }
        )
    }
}