package com.example.app.month_picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.app.R
import com.example.app.databinding.FragmentMonthPicker2Binding
import java.util.*

class MonthPicker2Fragment : DialogFragment() {

    private var _binding: FragmentMonthPicker2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthPicker2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builder = AlertDialog.Builder(requireActivity())
        val dialog = requireDialog().layoutInflater.inflate(R.layout.fragment_month_picker2, null)

        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1

        binding.startMonth.text = String.format("%d. %d", year, month)
        binding.currentYear.text = String.format("%d",year)

        binding.endDayBtn.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                binding.endMonth.visibility = View.VISIBLE
            }else{
                binding.endMonth.visibility= View.INVISIBLE
            }
        }
        binding.

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        binding.confirmBtn.setOnClickListener {
            dismiss()
        }

        builder.setView(dialog)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}