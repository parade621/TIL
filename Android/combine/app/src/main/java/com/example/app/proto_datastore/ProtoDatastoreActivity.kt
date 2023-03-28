package com.example.app.proto_datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app.R
import com.example.app.databinding.ActivityProtoDatastoreBinding

class ProtoDatastoreActivity : AppCompatActivity() {

    private val binding : ActivityProtoDatastoreBinding by lazy{
        ActivityProtoDatastoreBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}