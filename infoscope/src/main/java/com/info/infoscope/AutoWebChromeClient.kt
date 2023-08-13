package com.info.infoscope

import android.Manifest
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

internal class AutoWebChromeClient(
    private val activity : AppCompatActivity
) : WebChromeClient() {
    companion object{
        const val ACTIVITY_RESULT_CONTENT_LAUNCHER_KEY = "imageContentLauncher"
    }

    private var myOwnFilePath = ValueCallback<Array<Uri?>?> { }

    var mFilePathCallback: ValueCallback<Array<Uri?>?>? = null

    private var isAllPermissionsGranted = false

    private val getContentLauncher = activity.activityResultRegistry.register(
        ACTIVITY_RESULT_CONTENT_LAUNCHER_KEY,
        activity,
        ActivityResultContracts.GetContent()
    ) { selectedImageUri ->
        if (selectedImageUri != null) {
            mFilePathCallback?.onReceiveValue(arrayOf(selectedImageUri))
        }else{
            mFilePathCallback?.onReceiveValue(null)
        }
    }

    override fun onShowFileChooser(
        view: WebView?, filePath: ValueCallback<Array<Uri?>?>, fileChooserParams: FileChooserParams?
    ): Boolean {
        myOwnFilePath = filePath

        val permissionList = listOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        Dexter.withContext(activity).withPermissions(permissionList)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    isAllPermissionsGranted = p0?.areAllPermissionsGranted() == true
                    getImage()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()

        return true
    }

    private fun getImage() {
        mFilePathCallback = myOwnFilePath

        getContentLauncher.launch("image/*")
    }
}