package com.example.app.shared_prefs_singleton.dialog

import android.app.Dialog
import android.content.Context
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

class ProfileChooseDialog(context: Context) : Dialog(context) {

    private val binding: FragmentProfileChooseDialogBinding by lazy{
        FragmentProfileChooseDialogBinding.inflate(layoutInflater)
    }

    var onValueChangedListener: ((Int) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.blueProfile.setOnClickListener{
            val value = R.drawable.blue_profile
            onValueChangedListener?.invoke(value)
            dismiss()
        }
        binding.redProfile.setOnClickListener{
            val value = R.drawable.red_profile
            onValueChangedListener?.invoke(value)
            dismiss()
        }
        binding.orangeProfile.setOnClickListener{
            val value = R.drawable.orange_profile
            onValueChangedListener?.invoke(value)
            dismiss()
        }
    }
}