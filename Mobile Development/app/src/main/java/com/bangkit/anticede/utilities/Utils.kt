package com.bangkit.anticede.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val FILENAME_FORMAT = "dd-MMM-yyyy"


    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun createTempRecordFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir("Recordings")
        return File.createTempFile(timeStamp, ".aac", storageDir)
    }

    fun uriToFile(selectedVoice: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempRecordFile(context)

        val inputStream = contentResolver.openInputStream(selectedVoice) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }
}