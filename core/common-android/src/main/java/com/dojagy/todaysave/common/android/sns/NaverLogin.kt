package com.dojagy.todaysave.common.android.sns

import android.content.Context
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.common.util.DLog
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class NaverLogin(
    private val context: Context,
    private val onComplete: (snsKey: String, type: String, email: String, name: String?) -> Unit,
    private val onError: (msg: String) -> Unit,
    private val clientId: String,
    private val clientSecret: String,
    private val clientName: String
) {

    fun login() {
        NaverIdLoginSDK.initialize(
            context,
            clientId,
            clientSecret,
            clientName
        )
        startNaverLogin()
    }

    private fun startNaverLogin() {
        var naverToken: String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(result: NidProfileResponse) {
                val userId = result.profile?.id
                val email = result.profile?.email
                val nickname = result.profile?.nickname
                DLog.e("naverLogin", "id: $userId \ntoken: $naverToken")

                NaverIdLoginSDK.logout()
                if(userId != null) {
                    onComplete(userId, "NAVER", email ?: String.DEFAULT, nickname)
                }else {
                    onError("네이버 로그인에 실패했습니다.")
                }
            }

            override fun onFailure(httpStatus: Int, message: String) {
                onError("네이버 로그인에 실패했습니다.")
                DLog.e("NaverLoginError", message)
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        /** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                DLog.e("naverLogin", "oauthLoginCallback onSuccess")
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
//                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
//                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
//                var naverTokenType = NaverIdLoginSDK.getTokenType()
//                var naverState = NaverIdLoginSDK.getState().toString()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                onError("네이버 로그인에 실패했습니다.")
                DLog.e("naverLogin", "errorCode: ${errorCode}, errorDescription: $errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
    }
}