package com.dojagy.todaysave.data.source.api.service

import com.dojagy.todaysave.data.dto.MainDto
import com.dojagy.todaysave.data.dto.user.JoinRequestDto
import com.dojagy.todaysave.data.dto.user.LoginRequestDto
import com.dojagy.todaysave.data.dto.user.LoginResultDto
import com.dojagy.todaysave.data.dto.user.TokenDto
import com.dojagy.todaysave.data.dto.user.TokenReissueRequestDto
import com.dojagy.todaysave.data.dto.user.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @Headers("No-Auth: true")
    @POST("api/user/join")
    suspend fun join(
        @Body body: JoinRequestDto
    ): Response<MainDto<LoginResultDto>>

    @Headers("No-Auth: true")
    @GET("api/user/check-nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    ): Response<MainDto<Unit>>

    @Headers("No-Auth: true")
    @POST("api/user/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): Response<MainDto<LoginResultDto>>

    @Headers("No-Auth: true")
    @POST("api/user/token-reissue")
    suspend fun tokenReissue(
        @Body body: TokenReissueRequestDto
    ): Response<MainDto<TokenDto>>

    @GET("api/user/my-info")
    suspend fun myInfo(): Response<MainDto<UserDto>>

    @Headers("No-Auth: true")
    @GET("api/random-nickname")
    suspend fun randomNickname(): Response<MainDto<String>>
}