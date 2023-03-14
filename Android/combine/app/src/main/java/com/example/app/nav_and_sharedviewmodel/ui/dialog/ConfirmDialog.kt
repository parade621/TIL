package com.example.app.nav_and_sharedviewmodel.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.app.databinding.DialogConfirmBinding
import com.example.app.nav_and_sharedviewmodel.viewmodel.MyViewModel

class ConfirmDialog:DialogFragment() {

    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!
    private val myViewModel: MyViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogConfirmBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(true)

        binding.apply {
            viewModel = myViewModel
            confirmDialog = this@ConfirmDialog
            lifecycleOwner = viewLifecycleOwner
        }
    }

    fun onAddBtnClickListener(){
        myViewModel.setBeverageInc()
        Log.d("CustomDialog","onClicked")
    }
    fun onMinusClickListener(){
        myViewModel.setBeverageDec()
    }
    fun onConfirmListener(){
        myViewModel.setBeveragePrice()
        dialog?.dismiss()
    }
    fun onCancelListener(){
        dialog?.dismiss()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}