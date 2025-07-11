package com.dojagy.todaysave.data.source.api.interceptor

import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.data.domain.repository.TokenDatastoreRepository
import com.dojagy.todaysave.data.domain.repository.UserDatastoreRepository
import com.dojagy.todaysave.data.dto.user.TokenDto
import com.dojagy.todaysave.data.dto.user.TokenReissueRequestDto
import com.dojagy.todaysave.data.source.api.service.UserService
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenRepo: TokenDatastoreRepository,
    private val userRepo: UserDatastoreRepository,
    private val tokenApi: Lazy<UserService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 기존의 currentToken을 runBlocking 안에서 가져오도록 수정하여 최신성을 보장합니다.
        val currentToken = runBlocking { tokenRepo.currentToken } // suspend 함수를 호출하도록 변경

        // 리프레시 토큰이 없으면 갱신 시도조차 할 수 없으므로 포기.
        val refreshToken = currentToken?.refreshToken ?: return null
        val accessToken = currentToken.accessToken

        // 동시성 문제를 막기 위한 synchronized 블록
        synchronized(this) {
            // 다른 스레드에서 토큰 갱신을 성공시켜, 저장소의 토큰이 이미 바뀌었는지 확인.
            val latestToken = tokenRepo.currentToken
            if (latestToken != null && latestToken.accessToken != accessToken) {
                // 이미 토큰이 갱신되었다면, 새 토큰으로 요청을 다시 만들어 반환
                return newRequestWithToken(response.request, latestToken.accessToken)
            }

            // 이제 토큰 갱신을 시도한다.
            // Authenticator는 동기 컨텍스트이므로 runBlocking을 사용하여 suspend 함수 호출
            val newTokens: TokenDto? = runBlocking {
                try {
                    val reissueResponse = tokenApi.get().tokenReissue(
                        TokenReissueRequestDto(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    )

                    if (reissueResponse.isSuccessful && reissueResponse.body()?.status == "success") {
                        reissueResponse.body()?.data // 성공 시 새 토큰 DTO 반환
                    } else {
                        null // 실패 시 null 반환
                    }
                } catch (e: Exception) {
                    // 네트워크 오류 등 예외 발생 시 null 반환
                    e.printStackTrace()
                    null
                }
            }

            // 갱신된 토큰이 있는지 확인
            return if (newTokens != null) {
                // 갱신 성공: 새 토큰을 DataStore에 저장
                runBlocking {
                    tokenRepo.setTokens(
                        accessToken = newTokens.accessToken ?: String.Companion.DEFAULT,
                        refreshToken = newTokens.refreshToken ?: String.DEFAULT
                    )
                }
                // 원래 요청에 새 토큰을 담아 재시도
                newRequestWithToken(
                    request = response.request,
                    accessToken = newTokens.accessToken ?: String.DEFAULT
                )
            } else {
                runBlocking {
                    tokenRepo.clearTokens()
                    userRepo.clearUser()
                }
                null
            }
        }
    }

    private fun newRequestWithToken(request: Request, accessToken: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}