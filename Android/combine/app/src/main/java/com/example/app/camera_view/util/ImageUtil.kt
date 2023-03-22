package com.example.app.camera_view.util

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.core.impl.utils.Exif
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


object ImageUtil {

    // 이미지 파일의 EXIF 메타 정보를 분석 한 후, 이미지 회전 여부를 판단하는 함수
    fun rotate_check_EXIF(imagePath: String):Boolean{
        var rotate = false
        try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = true
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = true
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rotate
    }
    fun getOrientationFromBitmap(imagePath:String):Int{

        var exif: ExifInterface? = null

        try{
        exif = ExifInterface(imagePath)
        }catch (e:IOException){
            e.printStackTrace()
        }
        return exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
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

    // ExifInterface를 사용하여 EXIF 정보를 읽어오고, 회전 정보에 따라 Matrix 객체를 만들어 Bitmap을 회전시킨 후 반환
    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when(orientation){
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f,1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE ->{
                matrix.setRotate(90f)
                matrix.postScale(-1f,1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE ->{
                matrix.setRotate(-90f)
                matrix.postScale(-1f,1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return try{
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            bmRotated
        }catch (e: OutOfMemoryError){
            e.printStackTrace()
            null
        }
    }

    // 비트맵을 jpg이미지 파일로 저장
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