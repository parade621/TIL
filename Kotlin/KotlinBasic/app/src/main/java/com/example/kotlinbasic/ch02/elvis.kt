package com.example.kotlinbasic.ch02

fun main(){
    var data: String? = "Park"
    println("data = $data : ${data?.length ?: -1}")
    data = null
    println("data = $data : ${data?.length ?: -1}")
}