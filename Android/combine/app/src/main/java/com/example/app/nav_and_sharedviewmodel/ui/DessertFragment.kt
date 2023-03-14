package com.example.navnsharedviewmodel.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.navnsharedviewmodel.R
import com.example.navnsharedviewmodel.databinding.FragmentDessertBinding
import com.example.navnsharedviewmodel.viewmodel.MyViewModel

class DessertFragment :Fragment(){
    private var _binding : FragmentDessertBinding? = null
    private val binding get() = _binding!!
    private val myViewModel : MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDessertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            viewModel = myViewModel
            dessertFragment = this@DessertFragment
            lifecycleOwner = viewLifecycleOwner
        }
        Log.d("viewModel4", myViewModel.total.value.toString())
    }

    fun moveToCoffe(){
        findNavController().navigate(R.id.action_dessertFragment_to_coffeFragment)
    }

    fun moveToSummary(){
        findNavController().navigate(R.id.action_dessertFragment_to_summaryFragment)
    }

    fun makeToastMassage(){
        Toast.makeText(binding.root.context, "현재 화면입니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}