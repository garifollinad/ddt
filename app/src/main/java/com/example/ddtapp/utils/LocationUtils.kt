package com.example.ddtapp.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng

object LocationUtils {
    fun calculateDistance(currLocation: LatLng, houseLocation: LatLng): Double {
        val distance = FloatArray(Constants.CONST_2)
        if (currLocation.latitude != Constants.DISTANCE_ZERO && currLocation.longitude != Constants.DISTANCE_ZERO) {
            Location.distanceBetween(
                currLocation.latitude,
                currLocation.longitude,
                houseLocation.latitude,
                houseLocation.longitude,
                distance
            )
            return formatDouble(distance[0].toDouble() / Constants.DISTANCE_THOUSAND)
        }
        return Constants.DISTANCE_ZERO
    }

    // Rounds till one significant value after comma
    fun formatDouble(value: Double): Double {
        return Math.round(value * Constants.DISTANCE_TEN) / Constants.DISTANCE_TEN
    }

    // formats decimal to have comma after every three digits from end
    fun formatDecimalSeparator(number: Int?): String {
        return number.toString()
            .reversed()
            .chunked(Constants.CONST_3)
            .joinToString(",")
            .reversed()
    }
}