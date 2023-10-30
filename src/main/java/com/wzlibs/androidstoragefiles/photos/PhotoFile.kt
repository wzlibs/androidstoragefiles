package com.wzlibs.androidstoragefiles.photos

import com.wzlibs.androidstoragefiles.models.MediaFile

data class PhotoFile(
    override val id: Long,
    override val name: String,
    override val path: String,
    override val dateModifier: Long,
    override val size: Long
) : MediaFile