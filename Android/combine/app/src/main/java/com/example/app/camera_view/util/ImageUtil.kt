package com.example.app.camera_view.util

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.util.*


object ImageUtil {
    fun getBitmapFromUri(context: Context, uri: Uri?): Bitmap? {
        var bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

        return bitmap
    }

    fun getUriFromBitmap(context: Context, bitmap: Bitmap?):Uri?{
        val imageUri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues().apply {
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }
        )
        // Uri에 Bitmap을 저장
        imageUri?.let{ uri->
            context.contentResolver.openOutputStream(uri)?.use{outputStream ->
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }
        return imageUri
    }

    fun rotateBitmap(bitmap: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree)
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)

        return rotatedBitmap
    }

}