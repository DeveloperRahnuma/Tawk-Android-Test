package com.tawkto.myapplication.model

interface DataLoadStatus {
    fun onSucess()
    fun onFailed (str : String)
}