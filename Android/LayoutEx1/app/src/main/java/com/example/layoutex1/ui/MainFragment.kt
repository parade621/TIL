package com.example.layoutex1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.layoutex1.databinding.FragmentMainBinding

class MainFragment: Fragment(){

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TextViewModels by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            viewModel = viewModel
            mainFragment = this@MainFragment
        }
    }

    fun getText(){
        viewModel.getText(binding.tiInputEditText.text.toString())
        Log.d("wow","yes")
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}