package com.dojagy.todaysave.common.android.sns

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.dojagy.todaysave.common.util.DLog
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.toString

class GoogleLogin(
    private val context: Context,
    private val webKey: String,
    private val onComplete: (snsKey: String, type: String, email: String) -> Unit,
    private val onError: (msg: String) -> Unit
) {

    private lateinit var auth: FirebaseAuth

    private val credentialManager by lazy {
        CredentialManager.create(context)
    }

    private val googleIdOption by lazy {
        GetSignInWithGoogleOption
            .Builder(webKey)
            .build()
    }

    private val credentialRequest by lazy {
        GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    suspend fun login() {
        try {
            auth = FirebaseAuth.getInstance()

            val googleSignInRequest = credentialManager.getCredential(
                request = credentialRequest,
                context = context
            )

            val credential = googleSignInRequest.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                DLog.e("idToken", googleIdTokenCredential.idToken)
                DLog.e("id", googleIdTokenCredential.id)
                DLog.e("data", googleIdTokenCredential.data)
                DLog.e("givenName", googleIdTokenCredential.givenName)
                DLog.e("familyName", googleIdTokenCredential.familyName)
                DLog.e("displayName", googleIdTokenCredential.displayName)
                DLog.e("phoneNumber", googleIdTokenCredential.phoneNumber)
                DLog.e("profilePictureUri", googleIdTokenCredential.profilePictureUri)
                DLog.e("type", googleIdTokenCredential.type)

                googleSignInWithFirebaseAuth(
                    idToken = googleIdTokenCredential.idToken,
                    email = googleIdTokenCredential.id
                )
            }
        }catch (e: Exception) {
            DLog.e("googleLogin fail", e.localizedMessage)
            onError("구글 로그인에 실패했습니다.")
            e.printStackTrace()
        }
    }

    private fun googleSignInWithFirebaseAuth(
        idToken: String?,
        email: String?
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, email)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                DLog.e("userInfo", authResult.additionalUserInfo?.profile)
                DLog.e("email1", authResult?.user?.email)
                DLog.e("email2", email)
                DLog.e("uid", authResult.user?.uid)

                onComplete(authResult.user?.uid.toString(), "GOOGLE", authResult.user?.email ?: email.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    logout()
                }
            }
            .addOnFailureListener {
                DLog.e("firebaseAuth fail", it.localizedMessage)
                onError("구글 로그인에 실패했습니다.")
            }
            .addOnCanceledListener {
                DLog.e("firebaseAuth cancel", "cancel")
            }
    }

    private suspend fun logout() {
        credentialManager.clearCredentialState(request = ClearCredentialStateRequest())
        auth.signOut()
    }
}