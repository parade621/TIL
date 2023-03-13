package com.example.kotlinbasic.ch02

class MyClass{
    companion object{
        var data = 10
        fun some(){
            println(data)
        }
    }
}
fun main(){
    MyClass.data = 20
    MyClass.some()
}