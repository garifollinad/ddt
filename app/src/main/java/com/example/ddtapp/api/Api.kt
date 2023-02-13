package com.example.ddtapp.api

import com.example.ddtapp.model.House
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface Api {
    @GET("api/house")
    fun getHouses(
        @Header("Access-Key") key: String
    ): Single<Response<List<House>>>
}