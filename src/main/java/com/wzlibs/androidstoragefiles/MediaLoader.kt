package com.wzlibs.androidstoragefiles

import com.wzlibs.androidstoragefiles.models.MediaFile
import com.wzlibs.androidstoragefiles.models.MediaPack

interface MediaLoader {
    fun getMediaFiles(): List<MediaFile>

    fun getMediaPacks(): List<MediaPack>
}