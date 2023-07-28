package com.wzlibs.androidstoragefiles.utils

object Utils {
    fun getAlbumName(path: String): String {
        val splits = path.split("/")
        return splits[splits.size - 2]
    }
    fun getAlbumPath(path: String): String {
        return path.substring(0,path.lastIndexOf("/"))
    }
}