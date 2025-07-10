package com.dojagy.todaysave.data.dto.content

data class ContentSaveDto(
    val title: String,
    val description: String,
    val sharedUrl: String,
    val canonicalUrl: String,
    val memo: String,
    val thumbnailUrl: String,
    val folderId: Long,
    val categoryId: Long?,
    val tags: List<String>
)
