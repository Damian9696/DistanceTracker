package com.example.distancetracker.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

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
}