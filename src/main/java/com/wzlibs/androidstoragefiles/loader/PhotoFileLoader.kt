package com.wzlibs.androidstoragefiles.loader

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.wzlibs.androidstoragefiles.model.PhotoFile
import com.wzlibs.androidstoragefiles.model.SortType

class PhotoFileLoader(private val context: Context) {

    private val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    fun getPhotos(sortType: SortType = SortType.DESC): List<PhotoFile> {
        val photoFiles = ArrayList<PhotoFile>()
        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
        )
        val query = context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_MODIFIED} ${sortType.name}"
        )
        query?.use { cursor ->
            val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            val urlColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
            while (cursor.moveToNext()) {
                val url = cursor.getString(urlColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getLong(sizeColumn)
                val photoFile = PhotoFile(name, url, size)
                photoFiles.add(photoFile)
            }
            cursor.close()
        }
        return photoFiles
    }
}