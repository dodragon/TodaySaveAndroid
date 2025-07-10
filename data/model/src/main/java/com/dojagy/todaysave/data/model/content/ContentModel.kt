package com.dojagy.todaysave.data.model.content

import com.dojagy.todaysave.common.extension.DEFAULT
import java.time.LocalDateTime

data class ContentModel(
    val id: Long = Long.DEFAULT,
    val memo: String = String.DEFAULT,
    val sharedLink: String = String.DEFAULT,
    val createDt: LocalDateTime = LocalDateTime.now(),
    val link: LinkModel = LinkModel(),
    val tags: List<TagModel> = emptyList(),
    val category: CategoryModel? = null
)

data class LinkModel(
    val canonicalUrl: String = String.DEFAULT,
    val title: String = String.DEFAULT,
    val description: String = String.DEFAULT,
    val thumbnailUrl: String = String.DEFAULT,
)

data class TagModel(
    val name: String = String.DEFAULT
)

data class CategoryModel(
    val id: Long = Long.DEFAULT,
    val name: String = String.DEFAULT
)