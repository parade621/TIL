package Basic

import java.util.NoSuchElementException

class MyStack<T>(val num: T){

    private var top: StackNode<T>?

    init{
        val t: StackNode<T> = StackNode(num)
        top = t
    }

    // 링크드 리스트
    private class StackNode<T>(val data: T) {
        var next: StackNode<T>? = null
    }

    fun pop(): T {
        if (top == null) throw NoSuchElementException()
        val item: T = top!!.data
        top = top!!.next
        return item
    }

    fun push(item: T) {
        val t: StackNode<T> = StackNode<T>(item)
        t.next = top
        top = t
    }

    fun peek(): T {
        if (top == null) throw NoSuchElementException()
        return top!!.data
    }

    val isEmpty: Boolean
        get() = top == null
}

fun main(){

    var stack = MyStack<Int>(0)
    stack?.push(1)
    stack?.push(5)
    println(stack?.pop())
    println(stack?.pop())
    println(stack?.pop())
    println(stack?.isEmpty)
}