package com.example.navnsharedviewmodel.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.navnsharedviewmodel.databinding.CustomDialogBinding
import com.example.navnsharedviewmodel.viewmodel.MyViewModel

class CustomDialog: DialogFragment(){

    private var _binding: CustomDialogBinding? = null
    private val binding get() = _binding!!
    private val myViewModel : MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(true)
        binding.apply {
            viewModel = myViewModel
            customDialog = this@CustomDialog
            lifecycleOwner = viewLifecycleOwner
        }
    }

    fun addBtn(){
        myViewModel.cnt.value = myViewModel.cnt.value?.inc()
        Log.d("CustomeDilaog","clicked!")
    }

    fun minusBtn(){
        myViewModel.cnt.value = myViewModel.cnt.value?.dec()
        Log.d("CustomeDilaog","clicked!!")
    }

    fun cancleBtn(){
        dialog?.dismiss()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}