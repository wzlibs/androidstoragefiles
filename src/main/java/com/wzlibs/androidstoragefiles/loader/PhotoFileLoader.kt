package com.wzlibs.androidstoragefiles.loader

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import com.wzlibs.androidstoragefiles.model.AlbumPhoto
import com.wzlibs.androidstoragefiles.model.PhotoFile
import com.wzlibs.androidstoragefiles.model.SortType
import com.wzlibs.androidstoragefiles.utils.Utils
import java.io.File

class PhotoFileLoader(private val context: Context) {

    private val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    private val projection by lazy {
        arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
        )
    }

    private fun query(sortType: SortType = SortType.DESC): Cursor? {
        return context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_MODIFIED} ${sortType.name}"
        )
    }

    private fun getPhoto(
        cursor: Cursor,
        urlColumn: Int,
        nameColumn: Int,
        sizeColumn: Int
    ): PhotoFile {
        val path = cursor.getString(urlColumn)
        return PhotoFile(
            cursor.getString(urlColumn) ?: File(path).nameWithoutExtension,
            cursor.getString(nameColumn),
            cursor.getLong(sizeColumn)
        )
    }

    fun getPhotos(sortType: SortType = SortType.DESC): List<PhotoFile> {
        val photoFiles = ArrayList<PhotoFile>()
        query(sortType)?.use { cursor ->
            while (cursor.moveToNext()) {
                photoFiles.add(
                    getPhoto(
                        cursor, cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME),
                        cursor.getColumnIndex(MediaStore.Images.Media.DATA),
                        cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                    )
                )
            }
            cursor.close()
        }
        return photoFiles
    }

    fun getAlbumPhotos(sortType: SortType = SortType.DESC): List<AlbumPhoto> {
        val maps = HashMap<String, ArrayList<PhotoFile>>()
        query(sortType)?.use { cursor ->
            while (cursor.moveToNext()) {
                val photo = getPhoto(
                    cursor, cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME),
                    cursor.getColumnIndex(MediaStore.Images.Media.DATA),
                    cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                )
                val listPhotos = maps[Utils.getAlbumPath(photo.url)]
                if (listPhotos == null) {
                    maps[Utils.getAlbumPath(photo.url)] =
                        ArrayList<PhotoFile>().apply { add(photo) }
                } else {
                    listPhotos.add(photo)
                }
            }
            cursor.close()
        }
        val albumPhotos = ArrayList<AlbumPhoto>()
        maps.forEach {
            albumPhotos.add(AlbumPhoto(File(it.key).name, it.value))
        }
        return albumPhotos
    }

}