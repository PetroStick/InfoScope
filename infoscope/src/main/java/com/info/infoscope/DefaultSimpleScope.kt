package com.info.infoscope

import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class DefaultSimpleScope : Scope {

    private var onBuildUri: ((uri: Uri) -> Unit)? = null

    private val _buildUrlSharedFloat = MutableSharedFlow<String>()
    val buildUrlSharedFlow = _buildUrlSharedFloat.asSharedFlow()

    override fun buildSimpleInfoUri(
        domain: String,
        isHttps: Boolean,
        vararg property: Pair<String, String>,
        ) {
        val uri: Uri = buildUri(domain,isHttps, property.toList())

        CoroutineScope(Dispatchers.Main.immediate).launch {
            _buildUrlSharedFloat.emit(uri.toString())
        }
    }

    override fun buildSimpleInfoUri(
        domain: String,
        isHttps: Boolean,
        property: List<Pair<String, String>>,
    ) {
        val uri: Uri = buildUri(domain, isHttps, property )

        CoroutineScope(Dispatchers.Main.immediate).launch {
            _buildUrlSharedFloat.emit(uri.toString())
        }
    }

    override fun setInfoUrl(url: String) {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            _buildUrlSharedFloat.emit(url)
        }
    }

    override fun observeUri(onBuildUri: (uri: Uri) -> Unit) {
        this.onBuildUri = onBuildUri
    }

    private fun getScheme(isHttp: Boolean): String {
        return if (isHttp) "http" else "https"
    }

    private fun buildUri(
        domain: String,
        isHttps: Boolean,
        property: List<Pair<String, String>>,
    ): Uri {
        val uriBuilder = Uri.Builder()

        val uri = uriBuilder.apply {
            scheme(getScheme(isHttps))
            authority(domain)

            property.map {
                appendQueryParameter(it.first, it.second)
            }
        }.build()

        onBuildUri?.let { it(uri) }

        return uri
    }
}