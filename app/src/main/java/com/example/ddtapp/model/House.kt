package com.example.ddtapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class House (
    @PrimaryKey
    @SerializedName("id") val id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("price") val price : Int,
    @SerializedName("bedrooms") val bedrooms : Int,
    @SerializedName("bathrooms") val bathrooms : Int,
    @SerializedName("size") val size : Int,
    @SerializedName("description") val description : String,
    @SerializedName("zip") val zip : String,
    @SerializedName("city") val city : String,
    @SerializedName("latitude") val latitude : Int,
    @SerializedName("longitude") val longitude : Int,
    @SerializedName("createdDate") val createdDate : String
): Serializable