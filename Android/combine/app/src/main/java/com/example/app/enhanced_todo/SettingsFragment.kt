package com.example.app.enhanced_todo

import android.os.Bundle
import android.text.TextUtils
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.app.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings2, rootKey)
        val idPreference: EditTextPreference? = findPreference("id2")
        val colorPreference: ListPreference? = findPreference("color2")

        colorPreference?.summaryProvider =
            ListPreference.SimpleSummaryProvider.getInstance()
        idPreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    "설정이 되지 않았습니다."
                } else {
                    "설정된 ID 값은 $text 입니다."
                }
            }
    }
}