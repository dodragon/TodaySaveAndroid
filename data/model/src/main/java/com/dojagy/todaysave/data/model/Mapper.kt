package com.dojagy.todaysave.data.model

import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.data.dto.content.CategoryDto
import com.dojagy.todaysave.data.dto.content.ContentDto
import com.dojagy.todaysave.data.dto.content.LinkDto
import com.dojagy.todaysave.data.dto.content.MetadataDto
import com.dojagy.todaysave.data.dto.content.TagDto
import com.dojagy.todaysave.data.dto.user.TokenDto
import com.dojagy.todaysave.data.dto.user.UserDto
import com.dojagy.todaysave.data.model.content.CategoryModel
import com.dojagy.todaysave.data.model.content.ContentModel
import com.dojagy.todaysave.data.model.content.LinkModel
import com.dojagy.todaysave.data.model.content.MetadataModel
import com.dojagy.todaysave.data.model.content.TagModel
import com.dojagy.todaysave.data.model.user.Gender
import com.dojagy.todaysave.data.model.user.SnsType
import com.dojagy.todaysave.data.model.user.TokenModel
import com.dojagy.todaysave.data.model.user.UserGrade
import com.dojagy.todaysave.data.model.user.UserModel
import java.time.LocalDateTime

fun UserDto?.mapper(
    snsType: SnsType,
    snsKey: String
) = UserModel(
    id = this?.id ?: Long.DEFAULT,
    nickname = this?.nickname ?: String.DEFAULT,
    email = this?.email ?: String.DEFAULT,
    sns = snsType,
    snsKey = snsKey,
    birthday = this?.birthday,
    gender = if(this?.gender != null) {
        try {
            Gender.valueOf((this.gender ?: String.DEFAULT).uppercase())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        null
    },
    grade = if(this?.grade != null) {
        try {
            UserGrade.valueOf((this.grade ?: String.DEFAULT).uppercase())
        } catch (e: Exception) {
            e.printStackTrace()
            UserGrade.DEFAULT
        }
    }else {
        UserGrade.DEFAULT
    }
)

fun UserDto?.mapper() = UserModel(
    id = this?.id ?: Long.DEFAULT,
    nickname = this?.nickname ?: String.DEFAULT,
    email = this?.email ?: String.DEFAULT,
    birthday = this?.birthday,
    gender = try {
        Gender.valueOf((this?.gender ?: String.DEFAULT).uppercase())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    },
    grade = try {
        UserGrade.valueOf((this?.grade ?: String.DEFAULT).uppercase())
    } catch (e: Exception) {
        e.printStackTrace()
        UserGrade.DEFAULT
    }
)

fun TokenDto?.mapper() = TokenModel(
    accessToken = this?.accessToken ?: String.DEFAULT,
    refreshToken = this?.refreshToken ?: String.DEFAULT
)

fun ContentDto?.mapper() = ContentModel(
    id = this?.id ?: Long.DEFAULT,
    memo = this?.memo ?: String.DEFAULT,
    sharedLink = this?.sharedLink ?: String.DEFAULT,
    createDt = this?.createDt ?: LocalDateTime.now(),
    link = this?.link?.mapper() ?: LinkModel(),
    tags = this?.tags?.map { it?.mapper() ?: TagModel() } ?: emptyList(),
    category = this?.category?.mapper() ?: CategoryModel()
)

fun LinkDto?.mapper() = LinkModel(
    canonicalUrl = this?.canonicalUrl ?: String.DEFAULT,
    title = this?.title ?: String.DEFAULT,
    description = this?.description ?: String.DEFAULT,
    thumbnailUrl = this?.thumbnailUrl ?: String.DEFAULT
)

fun TagDto?.mapper() = TagModel(
    name = this?.name ?: String.DEFAULT
)

fun CategoryDto?.mapper() = CategoryModel(
    id = this?.id ?: Long.DEFAULT,
    name = this?.name ?: String.DEFAULT
)

fun MetadataDto?.mapper() = MetadataModel(
    url = this?.url ?: String.DEFAULT,
    title = this?.title ?: String.DEFAULT,
    description = this?.description ?: String.DEFAULT,
    thumbnailUrl = this?.thumbnailUrl ?: String.DEFAULT,
    faviconUrl = this?.faviconUrl ?: String.DEFAULT,
    canonicalUrl = this?.canonicalUrl ?: String.DEFAULT,
    linkType = this?.linkType ?: String.DEFAULT
)