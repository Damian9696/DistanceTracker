package com.example.distancetracker.ui.maps

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.distancetracker.R
import com.example.distancetracker.databinding.FragmentMapsBinding
import com.example.distancetracker.service.TrackerService
import com.example.distancetracker.util.Permissions.hasBackgroundLocationPermission
import com.example.distancetracker.util.Permissions.requestBackgroundLocationPermission
import com.example.distancetracker.util.ServiceEnum
import com.example.distancetracker.util.fadeAnimation
import com.example.distancetracker.util.scaleXYAnimation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createStartButton()
        createStopButton()
        createResetButton()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun createResetButton() {
        binding.resetButton.setOnClickListener { }
    }

    private fun createStopButton() {
        binding.stopButton.setOnClickListener { }
    }

    private fun createStartButton() {
        binding.startButton.setOnClickListener {
            onStartButtonClicked()
        }
    }

    private fun onStartButtonClicked() {
        context?.let { context ->
            if (hasBackgroundLocationPermission(context)) {
                binding.startButton.fadeAnimation(0f, 500)
                startCountDown()
            } else {
                requestBackgroundLocationPermission(this)
            }
        }
    }

    private fun startCountDown() {
        val timer: CountDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val currentSecond = millisUntilFinished / 1000
                if (currentSecond == 0L) {
                    context?.let { context ->
                        binding.timerTextView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                android.R.color.holo_green_light
                            )
                        )
                    }
                    binding.timerTextView.text = getString(R.string.map_go)
                } else {
                    binding.timerTextView.text = currentSecond.toString()
                }

                binding.timerCardView.scaleXYAnimation(0f, 1f, 900)
            }

            override fun onFinish() {
                binding.stopButton.fadeAnimation(1f, 500)
                sendActionCommandToService(ServiceEnum.ACTION_SERVICE_START)
            }

        }
        timer.start()
    }

    private fun sendActionCommandToService(serviceEnum: ServiceEnum) {
        context?.let { context ->
            Intent(
                context,
                TrackerService::class.java
            ).apply {
                this.action = serviceEnum.action
                context.startService(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            context?.let { context ->
                SettingsDialog.Builder(context).build().show()
            }
        } else {
            requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        onStartButtonClicked()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(_googleMap: GoogleMap) {
        googleMap = _googleMap
        googleMap.isMyLocationEnabled = true
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.uiSettings.apply {
            isZoomControlsEnabled = false
            isZoomGesturesEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false
            isCompassEnabled = false
            isScrollGesturesEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMyLocationButtonClick(): Boolean {
        binding.hintCardView.fadeAnimation(0f, 500)
        binding.startButton.fadeAnimation(1f, 500)
        return false
    }
}