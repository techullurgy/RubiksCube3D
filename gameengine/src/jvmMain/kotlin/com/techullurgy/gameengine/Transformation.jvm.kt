package com.techullurgy.gameengine

import java.util.Stack as JavaStack

actual class Stack<T> actual constructor() {
    private val stack = JavaStack<T>()

    actual fun push(value: T) {
        stack.push(value)
    }

    actual fun pop() {
        stack.pop()
    }

    actual fun peek(): T {
        return stack.peek()
    }

    actual fun empty(): Boolean {
        return stack.empty()
    }
}