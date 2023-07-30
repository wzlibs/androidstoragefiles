package com.wzlibs.androidstoragefiles.pdf

import com.wzlibs.androidstoragefiles.pdf.model.PdfFile
import com.wzlibs.androidstoragefiles.utils.Utils
import java.io.File


class PdfLoader {

    fun getPdfFiles(): ArrayList<PdfFile> {
        val pdfs = ArrayList<PdfFile>()
        val saveFiles = ArrayList<File>()
        Utils.loadAllFiles(Utils.externalStoragePath, saveFiles, "pdf").forEach {
            pdfs.add(PdfFile(it.nameWithoutExtension, it.path, it.lastModified(), it.length()))
        }
        return pdfs
    }

}