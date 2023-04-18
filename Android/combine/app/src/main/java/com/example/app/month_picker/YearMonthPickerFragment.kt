package com.example.app.month_picker

import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.app.R
import com.example.app.databinding.FragmentYearMonthPickerBinding
import com.example.app.month_picker.YearMonthPickerFragment.consts.MAX_YEAR
import com.example.app.month_picker.YearMonthPickerFragment.consts.MIN_YEAR
import java.util.*

class YearMonthPickerFragment : DialogFragment() {

    private var _binding: FragmentYearMonthPickerBinding? = null
    private val binding get() = _binding!!

    var listener: OnDateSetListener? = null
    var cal = Calendar.getInstance()
        private set

    private object consts {
        const val MAX_YEAR = 2099
        const val MIN_YEAR = 1975
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYearMonthPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater // 뭐지??

        val dialog = inflater.inflate(R.layout.fragment_year_month_picker, null)

        binding.cancel.setOnClickListener {
            dismiss()
        }
        binding.confirm.setOnClickListener {
            listener?.onDateSet(null, binding.pickerYear.value, binding.pickerMonth.value, 0)
            dismiss()
        }

        binding.pickerMonth.apply {
            minValue = 1
            maxValue = 12
            value = cal.get(Calendar.MONTH) + 1 // 디폴트로 현재 달을 가짐
            wrapSelectorWheel = false // 1 ~ 12 사이에서만 움직이도록 설정
        }

        binding.pickerYear.apply {
            minValue = MIN_YEAR
            maxValue = MAX_YEAR
            value = cal.get(Calendar.YEAR) // 디폴트로 현재 년(year)을 가짐.
            wrapSelectorWheel=false // MIN ~ MAX 사이에서만 움직이도록 설정
       }

        builder.setView(dialog)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}