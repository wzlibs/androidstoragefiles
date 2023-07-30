package com.wzlibs.androidstoragefiles.utils

import android.os.Environment
import java.io.File

object Utils {

    val externalStoragePath: String by lazy { Environment.getExternalStorageDirectory().absolutePath }

    fun getAlbumName(path: String): String {
        val splits = path.split("/")
        return splits[splits.size - 2]
    }

    fun getAlbumPath(path: String): String {
        return path.substring(0, path.lastIndexOf("/"))
    }

    fun loadAllFiles(path: String, saveList: ArrayList<File>, extension: String): List<File> {
        val f = File(path)
        f.listFiles()?.let { files ->
            files.forEach {
                if (it.isFile) {
                    if (it.extension == extension) {
                        saveList.add(it)
                        it.length()
                        it.lastModified()
                    }
                } else {
                    loadAllFiles(it.absolutePath, saveList, extension)
                }
            }
        }
        return saveList
    }

}