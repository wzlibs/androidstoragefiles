package com.wzlibs.androidstoragefiles

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
abstract class BaseLoader(private val context: Context) : MediaLoader {

    protected abstract val collection: Uri
    protected abstract val projection: Array<String>

    protected fun query(): Cursor? {
        return context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        )
    }

}