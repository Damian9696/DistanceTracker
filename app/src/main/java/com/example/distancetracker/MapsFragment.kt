package com.example.distancetracker

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ViewAnimator
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.distancetracker.databinding.FragmentMapsBinding
import com.example.distancetracker.util.fadeAnimation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

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
            Toast.makeText(requireContext(), "tetsstat", Toast.LENGTH_SHORT).show()
        }
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