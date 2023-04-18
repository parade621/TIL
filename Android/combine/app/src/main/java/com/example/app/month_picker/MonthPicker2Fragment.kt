package com.example.app.month_picker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.app.R
import com.example.app.databinding.FragmentMonthPicker2Binding

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