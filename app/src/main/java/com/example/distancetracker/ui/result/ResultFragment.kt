package com.example.distancetracker.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.distancetracker.databinding.FragmentResultBinding
import com.example.distancetracker.ext.fadeAnimationWithDelay
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class ResultFragment : BottomSheetDialogFragment() {

    private val args: ResultFragmentArgs by navArgs()

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createAnimations()
        createValues()
    }

    private fun createValues() {
        binding.distanceValueTextView.text = args.result.distance
        binding.timeValueTextView.text = args.result.time
    }

    private fun createAnimations() {
        lifecycleScope.launch {
            binding.resultTextView.fadeAnimationWithDelay(1f, 500, 300)
            binding.distanceGroupConstraintLayout.fadeAnimationWithDelay(1f, 500, 300)
            binding.timeGroupConstraintLayout.fadeAnimationWithDelay(1f, 500, 300)
            binding.shareButton.fadeAnimationWithDelay(1f, 500, 300)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}