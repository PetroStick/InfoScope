package com.info.infoscope

import android.content.Context
import android.view.View
import android.webkit.WebView

open class AutoWebView(context : Context) : WebView(context){
    fun close(){
        this.visibility = View.GONE
    }

    fun show(){
        this.visibility = View.VISIBLE
    }
}