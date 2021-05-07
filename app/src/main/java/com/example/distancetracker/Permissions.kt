package com.example.distancetracker

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.distancetracker.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions

object Permissions {

    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )


    fun requestLocationPermission(host: Fragment) {
        host.context?.let { context ->
            EasyPermissions.requestPermissions(
                host,
                rationale = context.getString(R.string.permission_location_rationale_message),
                requestCode = PERMISSION_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

}