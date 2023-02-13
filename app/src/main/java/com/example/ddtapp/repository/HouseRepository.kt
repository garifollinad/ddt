package com.example.ddtapp.repository

import com.example.ddtapp.api.Api
import com.example.ddtapp.model.House
import io.reactivex.Single
import javax.inject.Inject

interface HouseRepository {
    fun getHouses(): Single<List<House>>
}

class HouseRepositoryImpl @Inject constructor(
    private val api: Api,
): HouseRepository {
    override fun getHouses(): Single<List<House>> {
        return api.getHouses(key = "98bww4ezuzfePCYFxJEWyszbUXc7dxRx").flatMap { response ->
            if (response.isSuccessful) {
                val list = response.body()
                Single.just(list)
            } else {
                Single.error(Throwable(""))
            }
        }

    }
}