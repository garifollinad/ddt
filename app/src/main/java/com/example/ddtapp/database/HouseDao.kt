package com.example.ddtapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ddtapp.model.House
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface HouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHouses(houses: List<House>)

    @Query("SELECT * FROM house ORDER BY price ASC")
    fun getHouses(): Single<List<House>>

    @Query("SELECT * FROM house WHERE id = :id")
    fun getHouseById(id: String): Single<House?>

    @Query(
        """
        SELECT * FROM house
        WHERE (zip LIKE '%' || :zip || '%') AND (city LIKE '%' || :city || '%')
        ORDER BY price ASC
        """
    )
    fun getHousesZipAndCityFiltered(zip: String, city: String): Flowable<List<House>>

    @Query(
        """
        SELECT * FROM house
        WHERE (zip LIKE '%' || :filter || '%') OR (city LIKE '%' || :filter || '%') 
        ORDER BY price ASC
        """
    )
    fun getHousesZipOrCityFiltered(filter: String): Flowable<List<House>>
}