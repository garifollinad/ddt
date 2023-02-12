package com.example.ddtapp.base

sealed class Loading {
    object Show : Loading()
    object Hide : Loading()
}