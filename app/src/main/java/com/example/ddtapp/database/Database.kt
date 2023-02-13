package com.example.ddtapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ddtapp.model.House

@Database(entities = [House::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun houseDao(): HouseDao
}