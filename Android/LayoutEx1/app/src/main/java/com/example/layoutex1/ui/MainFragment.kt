package com.example.layoutex1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.layoutex1.adapter.InputTextListAdapter
import com.example.layoutex1.databinding.FragmentMainBinding
import com.example.layoutex1.model.TextViewModels

class MainFragment: Fragment(){

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TextViewModels by viewModels()
    private val textListAdapter by lazy{
        InputTextListAdapter(viewModel.textList)
    }

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
        setRecyclerView()
    }

    fun getText(){
        viewModel.getText(binding.tiInputEditText.text.toString())
        binding.tiInputEditText.setText("")
        textListAdapter.notifyDataSetChanged()
    }

    private fun setRecyclerView(){
        binding.rvItem.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = textListAdapter
        }
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}