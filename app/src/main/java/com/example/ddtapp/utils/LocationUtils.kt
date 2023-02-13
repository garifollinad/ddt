package com.example.ddtapp.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng

object LocationUtils {
    fun calculateDistance(currLocation: LatLng, houseLocation: LatLng): Double {
        val distance = FloatArray(2)
        if (currLocation.latitude != 0.0 && currLocation.longitude != 0.0) {
            Location.distanceBetween(
                currLocation.latitude,
                currLocation.longitude,
                houseLocation.latitude,
                houseLocation.longitude,
                distance
            )
            return formatDouble(distance[0].toDouble() / 1000)
        }
        return 0.0
    }

    // Rounds till one significant value after comma
    fun formatDouble(value: Double): Double {
        return Math.round(value * 10.0) / 10.0
    }

    // formats decimal to have comma after every three digits from end
    fun formatDecimalSeparator(number: Int?): String {
        return number.toString()
            .reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()
    }
}