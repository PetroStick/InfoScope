package com.info.infoscope

import android.net.Uri

interface Scope {
    fun buildSimpleInfoUri(domain: String, isHttps: Boolean, vararg property: Pair<String, String>)

    fun buildSimpleInfoUri(domain: String, isHttps: Boolean, property: List<Pair<String, String>>)

    fun setInfoUrl(url: String)

    fun observeUri(onBuildUri: (uri: Uri) -> Unit)
}