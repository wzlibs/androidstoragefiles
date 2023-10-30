package com.wzlibs.androidstoragefiles.videos

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.wzlibs.androidstoragefiles.BaseLoader
import com.wzlibs.androidstoragefiles.models.MediaFile
import com.wzlibs.androidstoragefiles.models.MediaPack
import com.wzlibs.androidstoragefiles.utils.Utils
import java.io.File

class VideoFileLoader(context: Context): BaseLoader(context) {

    override val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    override val projection by lazy {
        arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.SIZE,
        )
    }

    private fun getMedia(
        cursor: Cursor,
        idColumn: Int,
        urlColumn: Int,
        nameColumn: Int,
        dateModifierColumn: Int,
        sizeColumn: Int
    ): VideoFile {
        val path = cursor.getString(urlColumn)
        return VideoFile(
            cursor.getLong(idColumn),
            cursor.getString(urlColumn) ?: File(path).nameWithoutExtension,
            cursor.getString(nameColumn),
            cursor.getLong(dateModifierColumn),
            cursor.getLong(sizeColumn)
        )
    }

    override fun getMediaFiles(): List<MediaFile> {
        val videoFiles = ArrayList<VideoFile>()
        query()?.use { cursor ->
            while (cursor.moveToNext()) {
                videoFiles.add(
                    getMedia(
                        cursor,
                        cursor.getColumnIndex(MediaStore.Video.Media._ID),
                        cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME),
                        cursor.getColumnIndex(MediaStore.Video.Media.DATA),
                        cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED),
                        cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
                    )
                )
            }
            cursor.close()
        }
        return videoFiles
    }

    override fun getMediaPacks(): List<MediaPack> {
        val maps = HashMap<String, ArrayList<VideoFile>>()
        query()?.use { cursor ->
            while (cursor.moveToNext()) {
                val photo = getMedia(
                    cursor,
                    cursor.getColumnIndex(MediaStore.Video.Media._ID),
                    cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME),
                    cursor.getColumnIndex(MediaStore.Video.Media.DATA),
                    cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED),
                    cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
                )
                val listPhotos = maps[Utils.getAlbumPath(photo.path)]
                if (listPhotos == null) {
                    maps[Utils.getAlbumPath(photo.path)] =
                        ArrayList<VideoFile>().apply { add(photo) }
                } else {
                    listPhotos.add(photo)
                }
            }
            cursor.close()
        }
        val mediaPacks = ArrayList<MediaPack>()
        maps.forEach {
            mediaPacks.add(MediaPack(File(it.key).name, it.value))
        }
        return mediaPacks
    }

}