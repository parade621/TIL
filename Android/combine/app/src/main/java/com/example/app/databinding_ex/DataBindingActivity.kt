package com.example.app.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.app.R

class DataBindingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatabindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding)

        val factory = MyViewModelFactory(10, this)
        val myViewModel : MyViewModel by viewModels{factory}

        binding.lifecycleOwner= this
        binding.viewmodel = myViewModel

        binding.textView.text = myViewModel.counter.toString()

        binding.btn.setOnClickListener {
            myViewModel.liveCounter.value = myViewModel.liveCounter.value!!.plus(1)
        }
//        myViewModel.modifiedCounter.observe(this){counter->
//            binding.textView.text = counter.toString()
//        }
//        myViewModel.modifiedCounter.observe(this){counter->
//            binding.textView.text = counter
//        }
    }
}