package com.example.lottoinfo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.lottoinfo.R
import com.example.lottoinfo.databinding.FragmentLottoBinding
import com.example.lottoinfo.viewmodel.LottoViewModel

class LottoFragment :Fragment(){
    private var _binding : FragmentLottoBinding? = null
    private val binding get() = _binding!!
    private val myViewModel : LottoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLottoBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = myViewModel
            lottoFragment = this@LottoFragment
            lifecycleOwner = viewLifecycleOwner
        }
    }

    fun requestGETtoAPI(){
        val txt:String = binding.EtInputDrwNo.text.toString()
        myViewModel.getAPIResponse(txt)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}