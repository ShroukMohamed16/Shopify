package com.example.shopify.base

sealed class State<out T> {
    class Success<T>(val data: T) : State<T>()
    class Failure(val msg: Throwable) : State<Nothing>()
    object Loading : State<Nothing>()
}
