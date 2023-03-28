package com.example.app.shared_prefs_singleton.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.app.R
import com.example.app.databinding.FragmentProfileChooseDialogBinding
import com.example.app.shared_prefs_singleton.utils.Preferences

class ProfileChooseDialog : DialogFragment() {

    private var _binding: FragmentProfileChooseDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileChooseDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(true)

        binding.blueProfile.setOnClickListener{
            Preferences.userProfile = R.drawable.blue_profile
            dialog?.dismiss()
        }
        binding.redProfile.setOnClickListener{
            Preferences.userProfile = R.drawable.red_profile
            dialog?.dismiss()
        }
        binding.orangeProfile.setOnClickListener{
            Preferences.userProfile = R.drawable.orange_profile
            dialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}