package com.example.app.enhanced_todo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.app.R
import com.example.app.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_add_save -> {
            val inputData = binding.addEditView.text.toString()
            val db = DBHelper(this).writableDatabase
            try {
                db.execSQL(
                    "insert into TODO_TB (todo) values (?)",
                    arrayOf<String>(inputData)
                )
            } catch (e: Exception) {
                Log.e("Enhanced_ToDo", "Error inserting data into database", e)
            }
            db.close()
            val intent = intent
            intent.putExtra("result", inputData)
            setResult(Activity.RESULT_OK, intent)
            finish()
            true
        }
        else -> true
    }
}