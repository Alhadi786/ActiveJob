package com.xee.app.activejob.uitils

import okhttp3.ResponseBody

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(val isNetworkError: Boolean, val errorCode: Int?, val body: ResponseBody?=null, val errorMsg:String="") : Resource<Nothing>()
}