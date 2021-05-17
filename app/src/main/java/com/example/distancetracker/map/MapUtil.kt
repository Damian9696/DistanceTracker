package com.example.distancetracker.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat

class MapUtil(private val googleMap: GoogleMap) {

    fun setCameraPosition(latLng: LatLng): CameraPosition {
        return CameraPosition
            .builder()
            .target(latLng)
            .zoom(18f)
            .build()
    }

    fun followPolyline(locationList: MutableList<LatLng>) {
        if (locationList.isNotEmpty()) {
            googleMap.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(
                        setCameraPosition(
                            locationList.last()
                        )
                    ),
                1000,
                null
            )
        }
    }

    fun showBiggerPicture(locations: List<LatLng>) {
        val bounds = LatLngBounds.Builder()
        for (location in locations) {
            bounds.include(location)
        }
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(), 100
            ), 2000, null
        )
    }

    companion object {
        fun calculateTheDistance(locationList: List<LatLng>): String {
            if (locationList.isNotEmpty()) {
                val meters =
                    SphericalUtil.computeDistanceBetween(locationList.first(), locationList.last())

                return if (meters > 1000) {
                    val kilometers = meters / 1000
                    DecimalFormat("#.##km").format(kilometers)
                } else {
                    DecimalFormat("#m").format(meters)
                }
            }
            return "0m"
        }
    }
}