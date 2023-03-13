package com.example.kotlinbasic.ch02

fun hofFun(arg: (Int) -> Boolean): () -> String{
    val result = if(arg(10)){
        "valid"
    }else{
        "invalid"
    }
    return{"hofFun result : $result"}
}

fun main(){
    val result = hofFun { num -> num>0 }
    println(result())
}