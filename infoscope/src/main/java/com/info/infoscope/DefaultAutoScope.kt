package com.info.infoscope

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DefaultAutoScope(
    private val autoWebView: AutoWebView,
) : DefaultSimpleScope() {
    private var onBuildUrl: ((url: String?) -> Unit)? = null

    init {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            buildUrlSharedFlow.collectLatest { url ->
                onBuildUrl?.let { it(url) }

                autoWebView.loadUrl(url)
            }
        }
    }
}