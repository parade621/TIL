package com.example.app.sharedpref_ex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.app.R

class ASettingFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.a_setting, rootKey)

        val idPreference: EditTextPreference? = findPreference("id")
        idPreference?.isVisible = true

        idPreference?.summary = "code summary"
        idPreference?.title="code title"
    }
}
