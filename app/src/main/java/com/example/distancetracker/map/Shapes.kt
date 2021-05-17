package com.example.distancetracker.map

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class Shapes(private val googleMap: GoogleMap) {

    var polylineList = mutableListOf<Polyline>()

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

    fun removeAllPolylines() {
        for (polyline in polylineList) {
            polyline.remove()
        }
    }
}