package com.example.navnsharedviewmodel.data

import androidx.lifecycle.MutableLiveData
import com.example.navnsharedviewmodel.viewmodel.MenuItem

object DataSource {

    val MenuItems = mapOf(
        "americano" to
                MenuItem(
                    name = "americano",
                    price = 1500,
                    type = ItemType.BEVERAGE,
                    cnt = MutableLiveData(1)
                ),
        "latte" to
                MenuItem(
                    name = "latte",
                    price = 2800,
                    type=ItemType.BEVERAGE,
                    cnt = MutableLiveData(1)
                ),
        "greenTea" to
                MenuItem(
                    name = "greenTea",
                    price = 1800,
                    type = ItemType.BEVERAGE,
                    cnt = MutableLiveData(1)
                ),
        "macaron" to
                MenuItem(
                    name = "macaron",
                    price = 2000,
                    type = ItemType.DESSERT,
                    cnt = MutableLiveData(1)
                ),
        "cookies" to
                MenuItem(
                    name = "cookies",
                    price = 1500,
                    type = ItemType.DESSERT,
                    cnt = MutableLiveData(1)
                ),
        "cake" to
                MenuItem(
                    name = "cake",
                    price = 6000,
                    type = ItemType.DESSERT,
                    cnt = MutableLiveData(1)
                )
    )
}