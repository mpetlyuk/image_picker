package com.picker.file.factory

import com.picker.file.source.CameraPicker
import com.picker.file.source.FilePicker

object FileSourceFactory {

    @JvmStatic
    fun createImageSource(sourceType: FileSourceType): FilePicker {
        when (sourceType) {
            FileSourceType.CAMERA -> return CameraPicker()
            else -> throw RuntimeException()
        }
    }
}