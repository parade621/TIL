package com.example.app.camera_view.util

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
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

    fun saveBitmapToJpeg(context: Context, bitmap: Bitmap, name: String){
        val outputDirectory = File(context.getExternalFilesDir(null), "captured_images")
        if (!outputDirectory.exists()) outputDirectory.mkdirs()
        val photoFile = File(outputDirectory, name)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            photoFile
        ).build()

        try{
            //  빈 파일 생성
            photoFile.createNewFile()
            // 파일을 쓸 수 있는 스트림 준비
            val out = FileOutputStream(photoFile)
            //compress 함수를 사용해 스트림에 비트맴을 저장한다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

            out.close()
        }
        catch (e: FileNotFoundException){
            e.printStackTrace()
        }
        catch (e:IOException){
            e.printStackTrace()
        }

    }

}