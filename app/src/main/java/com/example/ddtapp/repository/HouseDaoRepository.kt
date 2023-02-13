package com.example.ddtapp.repository

import com.example.ddtapp.database.Database
import com.example.ddtapp.model.House
import io.reactivex.Single

interface HouseDaoRepository {
    fun getHouses(): Single<List<House>>
    fun insertHouses(houses: List<House>)
}

class HouseDaoRepositoryImpl(private val database: Database) :
    HouseDaoRepository {
    override fun getHouses(): Single<List<House>> {
        return database.houseDao().getHouses()
    }

    override fun insertHouses(houses: List<House>) {
        database.houseDao().insertHouses(houses)
    }

}