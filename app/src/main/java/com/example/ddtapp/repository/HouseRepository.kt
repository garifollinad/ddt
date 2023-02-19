package com.example.ddtapp.repository

import com.example.ddtapp.api.Api
import com.example.ddtapp.model.House
import com.example.ddtapp.utils.Constants
import io.reactivex.Single
import javax.inject.Inject

interface HouseRepository {
    fun getHouses(): Single<List<House>>
}

class HouseRepositoryImpl @Inject constructor(
    private val api: Api,
): HouseRepository {
    override fun getHouses(): Single<List<House>> {
        return api.getHouses(key = Constants.API_KEY).flatMap { response ->
            if (response.isSuccessful) {
                val list = response.body()
                Single.just(list)
            } else {
                Single.error(Throwable(""))
            }
        }

    }
}