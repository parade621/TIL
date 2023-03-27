//package com.example.app.doit_ch13
//
//import android.app.Activity
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import android.view.inputmethod.EditorInfo
//import com.example.app.R
//import com.example.app.databinding.ActivityAddBinding
//
//class AddActivity : AppCompatActivity() {
//    private val binding: ActivityAddBinding by lazy{
//        ActivityAddBinding.inflate(layoutInflater)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//    }
//
//    // 상단 액션바에 메뉴 옵션 생성
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_add, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    // 할 일을 등록
//    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
//        R.id.menu_add_save -> {
//            val intent = intent
//            intent.putExtra("result", binding.addEditView.text.toString())
//            setResult(Activity.RESULT_OK, intent)
//            finish()
//            true
//        }
//        else -> true
//    }
//
//}