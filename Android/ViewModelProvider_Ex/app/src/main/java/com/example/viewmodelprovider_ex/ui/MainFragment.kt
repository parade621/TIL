package com.example.viewmodelprovider_ex.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.viewmodelprovider_ex.R
import com.example.viewmodelprovider_ex.databinding.FragmentMainBinding
import com.example.viewmodelprovider_ex.viewmodel.MyViewModels

class MainFragment :Fragment(){

    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val myViewModel : MyViewModels by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewmodel = myViewModel
            mainFragment=this@MainFragment
        }
    }

    fun onClickBtn(){
        myViewModel.plusCount()
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }

}