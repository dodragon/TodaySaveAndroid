package com.dojagy.todaysave.common.android.sns

import android.content.Context
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.common.util.DLog
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaoLogin(
    private val context: Context,
    private val onComplete: (snsKey: String, type: String, email: String) -> Unit,
    private val onError: (msg: String) -> Unit
) {

    fun login() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    DLog.e("kakaoLogin", "로그인 실패 : $error");

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
                } else if (token != null) {
                    UserApiClient.instance.me { user, meError ->
                        if (meError != null) {
                            DLog.e("kakaoLogin", "사용자 정보 요청 실패 ${meError.message}")
                        } else if (user != null) {
                            DLog.e(
                                "kakaoLogin", "사용자 정보 요청 성공" +
                                        "\n회원번호: ${user.id}" +      //-> 키
                                        "\n이메일: ${user.kakaoAccount?.email}" +
                                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                        "\n성별: ${user.kakaoAccount?.gender?.name}" +
                                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                            )

                            val userKey = user.id
                            if(userKey != null) {
                                onComplete(userKey.toString(), "K", user.kakaoAccount?.email ?: String.DEFAULT)
                            }else {
                                onError("카카오 로그인에 실패했습니다.\n다른 로그인 방법을 이용해주세요.")
                            }
                        } else {
                            onError("카카오 로그인에 실패했습니다.\n다른 로그인 방법을 이용해주세요.")
                        }
                    }
                } else {
                    onError("카카오 로그인에 실패했습니다.\n다른 로그인 방법을 이용해주세요.")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
        }
    }


    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            error.printStackTrace()
            onError("카카오 로그인에 실패했습니다.\n다른 로그인 방법을 이용해주세요.")
        } else if (token != null) {
            DLog.e("kakaoLogin", "카카오계정으로 로그인 성공 ${token.accessToken}")
            UserApiClient.instance.me { user, meError ->
                if (meError != null) {
                    DLog.e("kakaoLogin", "사용자 정보 요청 실패 ${meError.message}")
                } else if (user != null) {
                    DLog.e(
                        "kakaoLogin", "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +  //키
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n성별: ${user.kakaoAccount?.gender?.name}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                    )

                    val userKey = user.id
                    UserApiClient.instance.logout {
                        if(userKey != null) {
                            onComplete(userKey.toString(), "KAKAO", user.kakaoAccount?.email ?: String.DEFAULT)
                        }else {
                            onError("카카오 로그인에 실패했습니다.\n다른 로그인 방법을 이용해주세요.")
                        }
                    }
                } else {
                    onError("카카오 로그인에 실패했습니다.\n다른 로그인 방법을 이용해주세요.")
                }
            }
        } else {
            onError("카카오 로그인에 실패했습니다.\n다른 로그인 방법을 이용해주세요.")
        }
    }
}