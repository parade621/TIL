package com.example.dialogtest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.dialogtest.databinding.FragmentBlankBinding

class BlankFragment :Fragment(){

    private var _binding : FragmentBlankBinding? = null
    private val binding get() = _binding!!
    private val myViewModel : MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        Log.d("Hello","world!")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            blankFragment = this@BlankFragment
        }
    }

    fun onClickBtn(){
        CustomDialog().show(childFragmentManager,"")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}