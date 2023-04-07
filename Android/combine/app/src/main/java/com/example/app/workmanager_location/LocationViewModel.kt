package com.example.app.workmanager_location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.Utils.DataStoreManager

class LocationViewModel : ViewModel() {

    val myLocation: MutableLiveData<String> = DataStoreManager.location

}