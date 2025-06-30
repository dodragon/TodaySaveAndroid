package com.dojagy.todaysave.common.extension

fun<T> List<T>.addAll(action: (List<T>) -> List<T>): List<T> {
    return this.toMutableList().apply { addAll(action(this)) }
}

fun<T> List<T>.add(action: (List<T>) -> T): List<T> {
    return this.toMutableList().apply { add(action(this)) }
}