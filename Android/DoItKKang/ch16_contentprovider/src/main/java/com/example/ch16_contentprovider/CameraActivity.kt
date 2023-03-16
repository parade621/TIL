package com.example.ch16_contentprovider

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ch16_contentprovider.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private val binding: ActivityCameraBinding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val requestCameraThumbnailLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val bitmap = it?.data?.extras?.get("data") as Bitmap
//            val calRatio = calculateInSampleSize(bitmap,
//            resources.getDimensionPixelSize(R.dimen.imgSize),
//            resources.getDimensionPixelSize(R.dimen.imgSize))
//
//
//            val ImageBitmap = BitmapFactory.decodeStream(bitmap, null)

            bitmap?.let {
                binding.ivCameraResult.setImageBitmap(bitmap)
                Log.d("CameraActivity", bitmap.height.toString())
            } ?: let {
                Log.d("CameraActivity", "bitmap null")
            }

        }

        binding.btnCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            requestCameraThumbnailLauncher.launch(intent)
        }

    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true // 옵션만 설정하고자 true로 지정
        // inJustDecodeBounds는 이미지 파일을 디코딩할 때, 이미지의 크기 정보만 추출하기 위해 사용되는 옵션
        // 사용 시, 이미지를 디코딩 하지 않고 이미지의 가로, 세로 크기 및 샘플링 비율 정보를 얻을 수 있다.
        // 이 코드에서는 이미지를 직접 불러오지 않고, 이미지의 각종 정보만 options에 설정한다.

        // openInputStream으로 바이트 데이터를 읽습니다.
        // contenResolver.openInPutStream.use()로 객체 사용 후 자동으로 닫아 줄 수도 있지만,
        // 객체를 안전하게 닫기위해 "try-catch_finally" 구문과 close() 메서드를 사용해 닫아줍니다.
        try {
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            // 각종 이미지 정보가 옵션에 설정된다.
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val (height: Int, width: Int) = options.run {
            outHeight to outWidth
        }
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}