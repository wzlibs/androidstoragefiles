package com.wzlibs.androidstoragefiles.models

interface MediaFile {
    val id: Long
    val name: String
    val path: String
    val dateModifier: Long
    val size: Long
}