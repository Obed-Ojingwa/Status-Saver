package com.obed.thestatussaver.repository




import java.io.File

object StatusRepository {

    private const val NORMAL_PATH = "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
    private const val BUSINESS_PATH = "/sto" +
            "rage/emulated/0/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses"

    private fun getStatusFiles(isBusiness: Boolean): List<File> {
        val path = if (isBusiness) BUSINESS_PATH else NORMAL_PATH
        val dir = File(path)

        return if (dir.exists() && dir.isDirectory) {
            dir.listFiles()
                ?.filter { it.isFile }
                ?.sortedByDescending { it.lastModified() } // ✅ newest first
                ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun getPhotos(isBusiness: Boolean): List<File> {
        return getStatusFiles(isBusiness).filter {
            it.extension.lowercase() in listOf("jpg", "jpeg", "png")
        }
    }

    fun getVideos(isBusiness: Boolean): List<File> {
        return getStatusFiles(isBusiness).filter {
            it.extension.lowercase() == "mp4"
        }
    }
}

/*

import android.os.Environment
import java.io.File

object StatusRepository {


    private val normalPath = Environment.getExternalStorageDirectory().absolutePath + "/WhatsApp/Media/.Statuses"
    private val businessPath = Environment.getExternalStorageDirectory().absolutePath + "/WhatsApp Business/Media/.Statuses"

    fun getStatusFiles(isBusiness: Boolean): List<File> {
        val path = if (isBusiness) {
            "/storage/emulated/0/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses"
        } else {
            "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
        }

        val dir = File(path)
        return if (dir.exists() && dir.isDirectory) {
            dir.listFiles()?.toList() ?: emptyList()
        } else emptyList()
    }


    fun getPhotos(isBusiness: Boolean): List<File> {
        return getStatusFiles(isBusiness).filter { it.extension.lowercase() in listOf("jpg", "png", "jpeg") }
    }

    fun getVideos(isBusiness: Boolean): List<File> {
        return getStatusFiles(isBusiness).filter { it.extension.lowercase() in listOf("mp4") }
    }
}
*/
