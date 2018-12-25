package com.picker.file.source

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import com.picker.file.FilePickerConstants
import com.picker.file.PickerResult
import com.picker.file.exceptions.PermissionNotGrantedException
import com.picker.file.exceptions.RepeatRequiresPermissionException
import com.picker.file.extentions.inBackground
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.functions.BiPredicate
import java.lang.IllegalStateException

//class GalleryPicker() : BaseFilePicker() {
//
//    val IMAGE_TYPE = "image/*"
//
//    override fun pickFile(activity: Activity): Single<PickerResult> {
//        val onSubscribe = RxGalleryPickerOnSubscribe(activity)
//        lifeCycleSet.add(onSubscribe)
//        return decorateSingle(activity, Single.create<PickerResult>(onSubscribe))
//
//    }
//
//    override fun pickFile(fragment: Fragment): Single<PickerResult> {
//        val onSubscribe = RxGalleryPickerOnSubscribe(fragment)
//        lifeCycleSet.add(onSubscribe)
//
//        fragment.context?.let { context ->
//            return decorateSingle(context, Single.create<PickerResult>(onSubscribe))
//        } ?: run {
//            return Single.error(IllegalStateException("Maybe fragment has been already detached"))
//        }
//    }
//
//    private fun decorateSingle(context: Context, input: Single<PickerResult>): Single<PickerResult> {
//        return input
//                .retry(BiPredicate { count, error ->
//                    return@BiPredicate error is RepeatRequiresPermissionException
//                })
//                .flatMap {
//                    return@flatMap filePathExtractor.getRealPath(context, it.filePath).getFile(context)
//                            .inBackground()
//                            .map { pickedFile -> PickerResult(Uri.fromFile(pickedFile)) }
//                }
//    }
//
//    private inner class RxGalleryPickerOnSubscribe(private val pickerContext: Any) : SingleOnSubscribe<PickerResult>, LifeCycle {
//
//        private var emitter: SingleEmitter<PickerResult>? = null
//
//        override fun subscribe(emitter: SingleEmitter<PickerResult>) {
//            this.emitter = emitter
//            try {
//                val permissionGranted = checkIfPermissionGranted(pickerContext, Manifest.permission.READ_EXTERNAL_STORAGE)
//                if (permissionGranted) {
//                    doPick(pickerContext, emitter)
//                } else {
//                    requestPermission(pickerContext, FilePickerConstants.REQUEST_CODE_STORAGE_PERMISSIONS, Manifest.permission.READ_EXTERNAL_STORAGE)
//                }
//            } catch (error: Throwable) {
//                if (!emitter.isDisposed) {
//                    emitter.onError(error)
//                }
//            }
//        }
//
//        override fun onSaveInstanceState(outState: Bundle) {
//        }
//
//        override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        }
//
//        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//            if (resultCode != Activity.RESULT_OK) {
//                return
//            }
//
//            when (requestCode) {
//                FilePickerConstants.REQUEST_CODE_GALLERY -> {
//                    emitter?.let { singleEmitter ->
//                        data?.data?.let { filePath ->
//                            singleEmitter.onSuccess(PickerResult(filePath))
//                        } ?: run {
//                            singleEmitter.onError(RuntimeException("There is no file result"))
//                        }
//                    }
//                }
//            }
//        }
//
//        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//            when (requestCode) {
//                FilePickerConstants.REQUEST_CODE_STORAGE_PERMISSIONS -> {
//                    emitter?.let { singleEmitter ->
//                        if (!singleEmitter.isDisposed) {
//                            val isPermissionGranted = isRequestedPermissionGranted(permissions, grantResults, Manifest.permission.READ_EXTERNAL_STORAGE)
//                            if (isPermissionGranted) {
//                                singleEmitter.onError(RepeatRequiresPermissionException())
//                            } else {
//                                singleEmitter.onError(PermissionNotGrantedException())
//                            }
//                        } else {
//                            lifeCycleSet.remove(this)
//                        }
//                    }
//                }
//            }
//        }
//
//        private fun doPick(pickerContext: Any, emitter: SingleEmitter<PickerResult>) {
//            if (pickerContext is Activity) {
//                pickerContext.startActivityForResult(createIntent(), FilePickerConstants.REQUEST_CODE_GALLERY)
//            } else if (pickerContext is Fragment && pickerContext.context != null) {
//                pickerContext.startActivityForResult(createIntent(), FilePickerConstants.REQUEST_CODE_GALLERY)
//            } else {
//                emitter.onError(java.lang.IllegalStateException())
//            }
//        }
//
//        private fun createIntent(): Intent {
//            return Intent.createChooser(Intent().setType(IMAGE_TYPE).setAction(Intent.ACTION_GET_CONTENT), "Image")
//        }
//    }
//
//    constructor(parcel: Parcel) : this()
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<GalleryPicker> {
//        override fun createFromParcel(parcel: Parcel): GalleryPicker {
//            return GalleryPicker(parcel)
//        }
//
//        override fun newArray(size: Int): Array<GalleryPicker?> {
//            return arrayOfNulls(size)
//        }
//    }
//}