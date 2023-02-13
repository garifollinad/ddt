package com.example.ddtapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ddtapp.model.House
import io.reactivex.Single

@Dao
interface HouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHouses(houses: List<House>)

    @Query("select * from house")
    fun getHouses(): Single<List<House>>
}