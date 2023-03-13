package com.example.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.databinding.FragmentBlankBinding

class BlankFragment :Fragment(){

    private var _binding :FragmentBlankBinding? =null
    private val binding get() = _binding!!
    private lateinit var myViewModels: MyViewModels

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlankBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModels = MyViewModels()
        binding.apply {
            viewModel = myViewModels
            blankFragment = this@BlankFragment
            lifecycleOwner = viewLifecycleOwner
        }
    }

    fun btnClickListner(){
        myViewModels.changeWords()
        //binding.tv.text = myViewModels.word.value.toString()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}