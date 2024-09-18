package uz.ibrohim.astracode

import android.annotation.SuppressLint
import android.webkit.WebView

@SuppressLint("SetJavaScriptEnabled")
fun webVIewSetting(webView: WebView){
    webView.apply {
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.databaseEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.javaScriptCanOpenWindowsAutomatically = true
    }
}