package com.example.distancetracker.map

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class Shapes(private val googleMap: GoogleMap) {

    var polylineList = mutableListOf<Polyline>()
    var markerList = mutableListOf<Marker>()

    fun drawPolyline(locationList: MutableList<LatLng>) {
        val polyline = googleMap.addPolyline(
            PolylineOptions().apply {
                width(10f)
                color(Color.BLUE)
                jointType(JointType.ROUND)
                startCap(ButtCap())
                endCap(ButtCap())
                addAll(locationList)
            }
        )

        polylineList.add(polyline)
    }

    fun drawMarker(position: LatLng) {
        val marker = googleMap.addMarker(
            MarkerOptions().position(position)
        )
        marker?.let {
            markerList.add(it)
        }
    }

    fun removeAllPolylines() {
        for (polyline in polylineList) {
            polyline.remove()
        }
        polylineList.clear()
    }

    fun removeAllMarkers() {
        for (marker in markerList) {
            marker.remove()
        }
        markerList.clear()
    }
}