package com.example.distancetracker.ui.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.distancetracker.R
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
        createShareButton()
    }

    private fun createShareButton() {
        binding.shareButton.setOnClickListener {
            shareResult()
        }
    }

    private fun shareResult() {
        context?.let { context ->
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    String.format(
                        context.getString(R.string.result_share_text),
                        args.result.distance,
                        args.result.time
                    )
                )
            }
            startActivity(shareIntent)
        }

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