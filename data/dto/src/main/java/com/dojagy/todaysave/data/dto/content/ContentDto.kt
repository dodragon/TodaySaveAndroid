package com.dojagy.todaysave.data.dto.content

import java.time.LocalDateTime

data class ContentDto(
    val id: Long?,
    val memo: String?,
    val sharedLink: String?,
    val createDt: LocalDateTime?,
    val link: LinkDto?,
    val tags: List<TagDto?>?,
    val category: CategoryDto?
)
