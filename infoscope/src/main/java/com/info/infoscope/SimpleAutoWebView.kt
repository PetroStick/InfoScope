package com.info.infoscope

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.View
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
@SuppressLint("ViewConstructor")
class SimpleAutoWebView(private val activity: AppCompatActivity) : AutoWebView(activity) {

    companion object {
        const val CACHE_SHARED_PREFERENCES_EXTRA = "autoCacheSharedPreferences"
        const val CACHE_EXTRA = "cache"
    }

    private val cacheSharedPreferences = activity.getSharedPreferences(
        CACHE_SHARED_PREFERENCES_EXTRA, Context.MODE_PRIVATE
    )

    private var cache : String? = null

    init {
        cache = cacheSharedPreferences.getString(CACHE_EXTRA , null)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setSimpleSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            importantForAutofill = IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        with(settings) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                saveFormData = true
            }

            cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
            userAgentString = userAgentString.replace("; wv", "")

            mixedContentMode = 0

            loadWithOverviewMode = true
            javaScriptEnabled = true
            savePassword = true
            builtInZoomControls = true
            useWideViewPort = true
            allowFileAccessFromFileURLs = true
            loadsImagesAutomatically = true
            displayZoomControls = false
            allowUniversalAccessFromFileURLs = true
            javaScriptCanOpenWindowsAutomatically = true
            allowContentAccess = true
            domStorageEnabled = true
            databaseEnabled = true
            allowFileAccess = true

            CookieManager.getInstance().setAcceptThirdPartyCookies(this@SimpleAutoWebView, true)
            CookieManager.getInstance().setAcceptCookie(true)

            setRenderPriority(android.webkit.WebSettings.RenderPriority.HIGH)
            setEnableSmoothTransition(true)
            setSupportMultipleWindows(false)
            setSupportZoom(false)
        }
    }

    fun setSimpleWebViewClient(
        onUrlLoaded: (String) -> Unit,
    ) {
        webViewClient = AutoWebViewClient(activity = activity)
        { url ->
            onUrlLoaded(url)
        }
    }

    fun cacheIfNot(url: String) {
        if(cache != null) return

        cacheSharedPreferences.edit().apply{
            putString(CACHE_EXTRA, url)
        }.apply()
    }

    fun getCache() : String?{
        return cache
    }

    fun setSimpleWebChromeClient() {
        webChromeClient = AutoWebChromeClient(activity)
    }
}