package com.example.ch17_database

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ch17_database.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSpActivity.setOnClickListener {
            val intent = Intent(this,SPActivity::class.java)
            startActivity(intent)
        }

//        val db = openOrCreateDatabase("testdb", Context.MODE_PRIVATE, null)
//
//        db.execSQL("Create table USER_TB(" +
//        "_id integer primary key autoincrement,"+
//        "name not null,"+
//                "phone)"
//        )
//        db.execSQL("insert into USER_TB (name, phone) values (?,?)",
//        arrayOf<String>("park","0101111")
//        )
//        val cursor = db.rawQuery("select * from USER_TB", null)
//
//        while(cursor.moveToNext()){
//            val name = cursor.getString(0)
//            val phone = cursor.getInt(1)
//        }
//
//        val values = ContentValues()
//        values.put("name", "park2")
//        values.put("phone","0101112")
//        db.insert("USER_TB",null, values)
//
//        val cursor2 = db.query("USER_TB", arrayOf<String>("name","phone"), "phone=?",
//        arrayOf<String>("0101112"),null,null,null)

        // --------------------------------------------

        // 파일 객체 생성 후 데이터 쓰기
        val file =  File(filesDir,"test.txt")
        val writeStream: OutputStreamWriter = file.writer()
        writeStream.write("hello world")
        writeStream.flush()

        val readStream: BufferedReader = file.reader().buffered()
        readStream.forEachLine{
            Log.d("park", "$it")
        }

        // Context 객체를 이용한 파일 i/o
        openFileOutput("test.txt", Context.MODE_PRIVATE).use{
            it.write("hello world!!".toByteArray())
        }
        openFileInput("test.txt").bufferedReader().forEachLine{
            Log.d("park", "$it")
        }

        // 외장 메모리
        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            Log.d("park", "ExternalStorageState MOUNTED")
        }else{
            Log.d("park","ExternalStorageState UNMOUNTED")
        }

        val file2: File? = getExternalFilesDir(null)
        Log.d("park", "${file?.absolutePath}")

        // 앱별 저장소에 파일 쓰기와 읽기
        val file3: File = File(getExternalFilesDir(null), "test.txt")
        val writeStream2: OutputStreamWriter = file3.writer()
        writeStream2.write("hello world")
        writeStream2.flush()

        // 파일 읽기
        val readStream2: BufferedReader = file3.reader().buffered()
        readStream2.forEachLine {
            Log.d("park", "$it")
        }

//        // 외장 메모리의 앱별 저장소 파일을 다른 앱에서 접근
//        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val file4 = File.createTempFile(
//            "JPEG_${timeStamp}_",
//            ".jpg",
//            storageDir
//        )
//        val filePath = file.absolutePath
//        // 파일 uri 획득
//        val photoURI : Uri = FileProvider.getUriForFile(
//            this,
//            "com.example.ch17_database", file4
//        )
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//        // 카메라 앱 실행 코드 입력

        // 공용 저장소에 접근
       val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        cursor?.let{
            while(cursor.moveToNext()){
                Log.d("park", "_id: ${cursor.getLong(0)}, name: ${cursor.getString(1)}")
            }
        }
    }
}