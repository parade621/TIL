package com.example.app.sharedpref_ex

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceScreen
import com.example.app.R
import com.example.app.databinding.ActivitySharedPrefBinding

class SharedPrefActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartScreenCallback,SharedPreferences.OnSharedPreferenceChangeListener {

    private val binding : ActivitySharedPrefBinding by lazy{
        ActivitySharedPrefBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val id = sharedPreferences.getString("color","")

    }

    override fun onPreferenceStartScreen(
        caller: PreferenceFragmentCompat,
        pref: PreferenceScreen
    ): Boolean {
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment!!
        )
        fragment.arguments = args
        supportFragmentManager.beginTransaction()
            .replace(R.id.setting_content, fragment)
            .addToBackStack(null)
            .commit()
        return true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == "id"){
            Log.i("park", "newValue : "+sharedPreferences?.getString("id",""))
        }
    }

    override fun onResume() {
        super.onResume()
    }
}