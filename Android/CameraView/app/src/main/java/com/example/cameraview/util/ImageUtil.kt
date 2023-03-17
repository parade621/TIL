package com.example.cameraview.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import java.io.File


object ImageUtil {

    fun getRotationDegrees(imagePath: String): Float {
        val exif = ExifInterface(imagePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        var rotationDegrees = 0
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                rotationDegrees = 90
            ExifInterface.ORIENTATION_ROTATE_180 ->
                rotationDegrees = 180
            ExifInterface.ORIENTATION_ROTATE_270 ->
                rotationDegrees = 270
        }
        return rotationDegrees.toFloat()
    }

    fun getRotationDegrees(file: File): Float {
        return getRotationDegrees(file.getAbsolutePath())
    }

    fun getRotationDegrees(context: Context?, uri: Uri?): Float {
        val path = getPathFromUri(context!!, uri!!)
        var rotationDegrees = 0f
        if (path != null) rotationDegrees = getRotationDegrees(path)

        return rotationDegrees
    }

    private fun getPathFromUri(context: Context, uri: Uri): String? {
        var result: String? = null
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = context.contentResolver.query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                var column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

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