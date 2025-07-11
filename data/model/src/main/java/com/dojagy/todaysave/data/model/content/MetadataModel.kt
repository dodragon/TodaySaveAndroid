package com.dojagy.todaysave.data.model.content

import com.dojagy.todaysave.common.extension.DEFAULT

data class MetadataModel(
    val url: String = String.DEFAULT,
    val title: String = String.DEFAULT,
    val description: String = String.DEFAULT,
    val thumbnailUrl: String = String.DEFAULT,
    val faviconUrl: String = String.DEFAULT,
    val canonicalUrl: String = String.DEFAULT,
    val linkType: String = "GENERAL"
)
