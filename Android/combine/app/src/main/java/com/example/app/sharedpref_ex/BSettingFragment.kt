package com.example.app.sharedpref_ex

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.app.R

class BSettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.b_setting, rootKey)

        val idPreference: EditTextPreference? = findPreference("id")
        val colorPreference: ListPreference? = findPreference("color")

//        idPreference?.summaryProvider=
//            EditTextPreference.SimpleSummaryProvider.getInstance()
        colorPreference?.summaryProvider=
            ListPreference.SimpleSummaryProvider.getInstance()
        idPreference?.summaryProvider=
            Preference.SummaryProvider<EditTextPreference>{ preference ->
                val text = preference.text
                if(TextUtils.isEmpty(text)){
                    "설정이 되지 않았습니다."
                }else{
                    "설정된 ID값은 : $text 입니다."
                }
            }
        idPreference?.setOnPreferenceClickListener { preference ->
            Log.d("park", "preference key : ${preference.key}")
            true
        }
        idPreference?.setOnPreferenceChangeListener { preference, newValue ->
            Log.d("park", "preference key: ${preference.key} , newValue: $newValue")
            true
        }

    }

}