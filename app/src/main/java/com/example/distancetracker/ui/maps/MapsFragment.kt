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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.distancetracker.R
import com.example.distancetracker.databinding.FragmentMapsBinding
import com.example.distancetracker.ext.changeTextColor
import com.example.distancetracker.ext.fadeAnimation
import com.example.distancetracker.ext.scaleXYAnimation
import com.example.distancetracker.map.MapUtil
import com.example.distancetracker.map.Shapes
import com.example.distancetracker.model.Result
import com.example.distancetracker.service.TrackerService
import com.example.distancetracker.model.Time
import com.example.distancetracker.util.*
import com.example.distancetracker.util.Permissions.hasBackgroundLocationPermission
import com.example.distancetracker.util.Permissions.requestBackgroundLocationPermission
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    EasyPermissions.PermissionCallbacks, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap

    private val shapes by lazy { Shapes(googleMap) }
    private val mapCamera by lazy { MapUtil(googleMap) }
    private val time by lazy { Time() }

    private var locations = mutableListOf<LatLng>()

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

        bindLifecycleOwner()

        createStartButton()
        createStopButton()
        createResetButton()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun bindLifecycleOwner() {
        binding.lifecycleOwner = this
    }

    private fun createResetButton() {
        binding.resetButton.setOnClickListener {
            onResetButtonClicked()
        }
    }

    private fun onResetButtonClicked() {
        mapReset()
    }

    private fun mapReset() {
        TrackerService.getLastKnownLocation()
        shapes.removeAllPolylines()
        shapes.removeAllMarkers()
        locations.clear()
        binding.resetButton.fadeAnimation(0f, 500)
        binding.startButton.fadeAnimation(1f, 500)
    }

    private fun createStopButton() {
        binding.stopButton.setOnClickListener {
            onStopButtonClicked()
        }
    }

    private fun onStopButtonClicked() {
        sendActionCommandToService(ServiceEnum.ACTION_SERVICE_STOP)
        binding.stopButton.fadeAnimation(0f, 500)
    }

    private fun displayResult() {
        val result = Result(
            MapUtil.calculateTheDistance(locations),
            TimeUtil.calculateElapsedTime(time.start, time.stop)
        )
        lifecycleScope.launch {
            delay(2500)
            findNavController().navigate(
                MapsFragmentDirections.actionMapsFragmentToResultFragment(
                    result
                )
            )
            binding.resetButton.fadeAnimation(1f, 500)
        }
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
                    binding.timerTextView.changeTextColor(android.R.color.holo_green_light)
                    binding.timerTextView.text = getString(R.string.map_go)
                } else {
                    binding.timerTextView.changeTextColor(android.R.color.holo_red_light)
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
        googleMap.setOnMarkerClickListener(this)
        googleMap.uiSettings.apply {
            isZoomControlsEnabled = false
            isZoomGesturesEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false
            isCompassEnabled = false
            isScrollGesturesEnabled = false
        }
        subscribeTrackerService()
    }

    private fun subscribeTrackerService() {

        binding.trackerService = TrackerService()

        TrackerService.locationList.observe(viewLifecycleOwner) {
            it?.let { locationList ->
                shapes.drawPolyline(locationList)
                mapCamera.followPolyline(locationList)
                locations = locationList
            }
        }

        TrackerService.startTime.observe(viewLifecycleOwner) {
            it?.let { startTime ->
                time.start = startTime
            }
        }

        TrackerService.stopTime.observe(viewLifecycleOwner) {
            it?.let { stopTime ->
                stopTimeLogic(stopTime)
            }
        }

        TrackerService.lastKnownLocation.observe(viewLifecycleOwner) {
            it?.let {
                mapCamera.setCameraPosition(it)
            }
        }

    }

    private fun stopTimeLogic(stopTime: Long) {
        time.stop = stopTime

        if (stopTime != 0L) {
            mapCamera.showBiggerPicture(locations)
            shapes.drawMarker(locations.first())
            shapes.drawMarker(locations.last())
            displayResult()
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

    override fun onMarkerClick(p0: Marker): Boolean {
        return true
    }
}