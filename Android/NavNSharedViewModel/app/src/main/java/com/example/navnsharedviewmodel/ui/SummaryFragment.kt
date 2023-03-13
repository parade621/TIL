package com.example.navnsharedviewmodel.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.navnsharedviewmodel.databinding.FragmentSummaryBinding
import com.example.navnsharedviewmodel.viewmodel.MyViewModel

class SummaryFragment :Fragment(){

    private var _binding : FragmentSummaryBinding? = null
    private val binding get() = _binding!!
    private val myViewModel : MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = myViewModel
            summaryFragment = this@SummaryFragment
            lifecycleOwner = viewLifecycleOwner
        }

        Log.d("viewModel3",myViewModel.total.value.toString())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
