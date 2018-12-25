package com.picker.file

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.picker.file.factory.FileSourceFactory
import com.picker.file.factory.FileSourceType
import com.picker.file.rx.AsyncSingleSubject
import com.picker.file.source.FilePicker
import com.picker.file.source.LifeCycle
import io.reactivex.subjects.Subject

open class RxFilePicker : LifeCycle {

    private val pickerResultSubject = AsyncSingleSubject.create<PickerResult>()
    private var picker: FilePicker? = null

    fun fromSource(sourceType: FileSourceType): RxFilePicker {
        picker = FileSourceFactory.createFilePicker(sourceType)
        picker?.pickerResultSubject = pickerResultSubject
        return this
    }

    fun pickFile(activity: Activity) = picker?.pickFile(activity)
            ?: throw RuntimeException("Picker is not initialized")

    fun pickFile(fragment: Fragment) = picker?.pickFile(fragment)
            ?: throw RuntimeException("Picker is not initialized")

    fun getPickerFileReady(): Subject<PickerResult> = pickerResultSubject

    fun onSaveInstanceState(outState: Bundle) {
        picker?.let { filePicker -> outState.putParcelable(FilePickerConstants.ARG_PICKER, picker) }
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        picker = savedInstanceState.getParcelable(FilePickerConstants.ARG_PICKER) as FilePicker?
        picker?.pickerResultSubject = pickerResultSubject
    }

    override fun onRequestPermissionsResult(pickerContext: Any, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        picker?.onRequestPermissionsResult(pickerContext, requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        picker?.onActivityResult(requestCode, resultCode, data)
    }
}
