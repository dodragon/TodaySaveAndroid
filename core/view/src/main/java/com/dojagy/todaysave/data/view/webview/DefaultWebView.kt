package com.dojagy.todaysave.data.view.webview

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun DefaultWebView(
    modifier: Modifier = Modifier,
    userAgent: String? = null,
    url: String
) {
    AndroidView(
        modifier = modifier,
        factory = {
            WebView(it).apply {
                defaultWebView(
                    userAgent = userAgent
                )
            }
        },
        update = {
            it.loadUrl(url)
        }
    )
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.defaultWebView(
    userAgent: String? = null,
): WebView {
    this.webViewClient = object : WebViewClient() {}
    this.webChromeClient = object : WebChromeClient() {}
    this.settings.apply {
        domStorageEnabled = true                                     //Local storage
        javaScriptEnabled = true                                     //Js setting
        builtInZoomControls = false                                  //Android Zoom icon
        cacheMode = WebSettings.LOAD_DEFAULT                        //LOAD_NO_CACHE
        displayZoomControls = true                                   //Pinch zoom in & out
        textZoom = 100                                               //No use Android System font size
        javaScriptCanOpenWindowsAutomatically = false                 //window.open() 허용
        setSupportMultipleWindows(false)                              //Multi Window
        loadWithOverviewMode = true                                  //컨텐츠가 웹뷰보다 클때 스크린크기에 맞추기
        useWideViewPort = true                                       //wide viewport 맞춤
        mixedContentMode = WebSettings.LOAD_DEFAULT
        setSupportZoom(true)
        layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        defaultTextEncodingName = "UTF-8"

        userAgent?.let {
            userAgentString = it
        }
    }

    return this
}