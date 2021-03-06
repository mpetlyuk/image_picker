package com.picker.file.source

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import com.picker.file.PickerResult
import io.reactivex.subjects.Subject

abstract class BaseFilePicker : FilePicker {

    protected var resultSubject: Subject<PickerResult>? = null

    override fun setResultCallback(callback: Subject<PickerResult>?) {
        resultSubject = callback
    }

    internal fun requestPermission(pickerContext: Any, requestCode: Int, vararg permission: String) {
        if (pickerContext is Activity) {
            ActivityCompat.requestPermissions(pickerContext, permission, requestCode)
        }

        if (pickerContext is Fragment) {
            pickerContext.requestPermissions(permission, requestCode)
        }
    }

    @Throws(IllegalArgumentException::class)
    internal fun checkIfPermissionGranted(pickerContext: Any, permission: String): Boolean {
        var determineContext: Context? = null

        if (pickerContext is Activity) {
            determineContext = pickerContext
        }

        if (pickerContext is Fragment) {
            determineContext = pickerContext.context
        }

        return determineContext?.let { safeContext ->
            return ActivityCompat.checkSelfPermission(safeContext, permission) == PackageManager.PERMISSION_GRANTED
        } ?: run {
            throw IllegalArgumentException("Context is not determined")
        }
    }

    internal fun isRequestedPermissionGranted(permissions: Array<out String>, grantResults: IntArray, requestedPermission: String): Boolean {
        try {
            return grantResults[permissions.indexOf(requestedPermission)] == PackageManager.PERMISSION_GRANTED
        } catch (error: Throwable) {
            return false
        }
    }
}