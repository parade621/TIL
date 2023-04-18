package com.example.app.month_picker

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import com.example.app.R
import com.example.app.databinding.ActivityMonthPickerBinding

class MonthPickerActivity : AppCompatActivity() {

    private val binding : ActivityMonthPickerBinding by lazy{
        ActivityMonthPickerBinding.inflate(layoutInflater)
    }

    private val d = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        Toast.makeText(this, "${year}년 ${month}월이 선택됨", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnYearMonthPicker.setOnClickListener {
            val pd = YearMonthPickerFragment()
            pd.listener = d
            pd.show(supportFragmentManager, "DialogTest")
        }

    }


}