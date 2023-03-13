package com.example.navnsharedviewmodel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navnsharedviewmodel.data.DataSource

class MyViewModel:ViewModel() {

    private val _total:MutableLiveData<Int> = MutableLiveData()
    val total: LiveData<Int> get() = _total

    private val _beverage= MutableLiveData<MenuItem?>()
    val beverage:LiveData<MenuItem?> = _beverage

    private val _desset: MutableLiveData<MenuItem?> = MutableLiveData()
    val dessert:LiveData<MenuItem?> = _desset

    private val _dessert_cnt : MutableLiveData<Int> = MutableLiveData()
    val dessert_cnt:LiveData<Int> = _dessert_cnt

    val cnt: MutableLiveData<Int> = MutableLiveData(1)

    var menuItem = DataSource.MenuItems
    private var previousBeveragePrice = 0
    private var previousDessertPrice = 0

    init {
        resetValue()
    }

    fun resetValue(){
        // 기본값은 0으로 초기화
        _total.value = 0
        if (_beverage.value != null) {
            DataSource.MenuItems[_beverage.value?.name]?.cnt?.value = 1
        }
        _beverage?.value = null
        _desset?.value = null
        _dessert_cnt.value = 0
    }

    fun setBeverage(beverage: String){
        if (_beverage.value != null){
            previousBeveragePrice = (_beverage.value!!.price * _beverage.value!!.cnt.value!!.toInt())
            _beverage.value = menuItem[beverage]
            updateTotal(-previousBeveragePrice)
        }else{
            _beverage.value = menuItem[beverage]
        }
        test()
    }

    private fun test(){
        if (menuItem["americano"]!!.equals(_beverage.value)){
        }
    }

    fun setBeverageInc(){
        _beverage.value!!.cnt.value = _beverage.value!!.cnt.value!!.inc()
    }
    fun setBeverageDec(){
        _beverage.value!!.cnt.value = _beverage.value!!.cnt.value!!.dec()
    }

    fun setBeveragePrice(){
        updateTotal(_beverage.value!!.price * _beverage.value!!.cnt.value!!.toInt())
    }

    fun setDessert(dessert: String){
        if(_desset.value != null){
            previousDessertPrice = (_desset.value!!.price * _desset.value!!.cnt.value!!.toInt())
            _desset.value = menuItem[dessert]
            updateTotal(-previousDessertPrice)
        }else{
            _desset.value = menuItem[dessert]
        }
        updateTotal(_desset.value!!.price * _desset.value!!.cnt.value!!.toInt())
    }

    private fun updateTotal(price : Int){
        _total.value = _total.value?.plus(price)
    }

}