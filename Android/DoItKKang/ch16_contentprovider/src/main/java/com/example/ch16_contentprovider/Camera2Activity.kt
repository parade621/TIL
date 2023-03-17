package com.example.ch16_contentprovider

import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.ch16_contentprovider.databinding.ActivityCamera2Binding
import java.io.File
import java.util.*

class Camera2Activity : AppCompatActivity() {

    private val binding : ActivityCamera2Binding by lazy{
        ActivityCamera2Binding.inflate(layoutInflater)
    }

    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val temp = externalMediaDirs
        val file = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
        filePath = file.absolutePath

        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            val option = BitmapFactory.Options()
            option.inSampleSize=10
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            binding.ivCameraResult.setImageBitmap(bitmap)
        }

        binding.btnCamera.setOnClickListener {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.ch16_contentprovider.fileprovider",
                file
            )

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            requestCameraFileLauncher.launch(intent)
        }


    }


}