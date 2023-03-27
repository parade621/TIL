//package com.example.app.doit_ch13
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.os.PersistableBundle
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.app.R
//import com.example.app.databinding.ActivityMain13Binding
//import com.example.ch13_activity.MyAdapter
//
//class Main13Activity : AppCompatActivity() {
//
//    private val binding:ActivityMain13Binding by lazy{
//        ActivityMain13Binding.inflate(layoutInflater)
//    }
//    var datas: MutableList<String>? = null
//    lateinit var myAdapter: MyAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//
//        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()){
//            it.data!!.getStringExtra("result")?.let{
//                datas?.add(it)
//                myAdapter.notifyDataSetChanged()
//            }
//        }
//
//
//        // 할 일 추가 엑티비티 실행
//        binding.mainFab.setOnClickListener {
//            val intent = Intent(this, AddActivity::class.java)
//            requestLauncher.launch(intent)
//        }
//
//        // Bundle에 정보를 저장한다. 50kb 이하의 데이터만 저장하는 것을 권장
//        datas = savedInstanceState?.let{
//            it.getStringArrayList("datas")?.toMutableList()
//        } ?: let{
//            mutableListOf<String>()
//        }
//
//        // 만들어진 To-Do를 리사이클러 뷰로 보여준다.
//        binding.mainRecyclerView.apply{
//            layoutManager = LinearLayoutManager(binding.root.context)
//            myAdapter= MyAdapter(datas)
//            adapter = myAdapter
//            addItemDecoration(
//                DividerItemDecoration(binding.root.context, LinearLayoutManager.VERTICAL))
//        }
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putStringArrayList("datas", ArrayList(datas))
//    }
//
//}