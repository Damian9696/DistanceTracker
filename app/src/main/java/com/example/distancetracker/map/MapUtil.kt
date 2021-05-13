package com.example.distancetracker.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat

class MapCamera(private val googleMap: GoogleMap) {

    private fun setCameraPosition(latLng: LatLng): CameraPosition {
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

    fun calculateTheDistance(locationList: MutableList<LatLng>): String {
        if (locationList.isNotEmpty()) {
            val meters =
                SphericalUtil.computeDistanceBetween(locationList.first(), locationList.last())

            return if (meters > 1000) {
                val kilometers = meters / 1000
                DecimalFormat("#.##km").format(kilometers)
            } else {
                "${meters}m"
            }
        }
        return "0m"
    }
}