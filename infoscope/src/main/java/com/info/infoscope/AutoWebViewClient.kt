package com.info.infoscope

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient

internal class AutoWebViewClient(
    private val activity: Activity,
    private val onPageLoaded: (String) -> Unit,
) : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        if (url == null) return

        onPageLoaded(url)
    }

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(
        view: WebView,
        url: String
    ): Boolean {
        try {
            if (url.startsWith("https://t.me/joinchat")) {
                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    activity.startActivity(this)
                }

                return true
            }

            return if (url.startsWith("http://") || url.startsWith("https://")) {
                false
            } else {
                if (url.startsWith(WebView.SCHEME_MAILTO)) {
                    Intent(Intent.ACTION_SEND).apply {
                        type = "plain/text"
                        putExtra(
                            Intent.EXTRA_EMAIL,
                            url.replace(WebView.SCHEME_MAILTO, "")
                        )
                        Intent.createChooser(this, "Mail")
                            .run {
                                activity.startActivity(this)
                            }
                    }
                }

                if (url.startsWith(WebView.SCHEME_TEL)) {
                    Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse(url)

                        Intent.createChooser(this, "Call")
                            .run {
                                activity.startActivity(this)
                            }
                    }
                }

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

                activity.startActivity(intent)

                true
            }

        } catch (e: Exception) {
            return true
        }
    }

}