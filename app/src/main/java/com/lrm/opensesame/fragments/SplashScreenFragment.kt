package com.lrm.opensesame.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lrm.opensesame.R
import com.lrm.opensesame.databinding.FragmentSplashScreenBinding
import com.lrm.opensesame.utils.BiometricAuthListener
import com.lrm.opensesame.utils.BiometricUtils

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment(), BiometricAuthListener {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    // Lazily initialize the animation to animate the App's title
    private val appNameAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.app_name_anim)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = Color.WHITE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.fingerprintIv.visibility = View.VISIBLE
        }, 400)

        Handler(Looper.getMainLooper()).postDelayed({
            if (BiometricUtils.isBiometricReady(requireContext())) {
                BiometricUtils.showBiometricPrompt(
                    fragment = this,
                    listener = this,
                    cryptoObject = null
                    )
            } else {
                val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
                this.findNavController().navigate(action)
                Toast.makeText(requireContext(), "Biometric feature not available on this device...", Toast.LENGTH_SHORT).show()
            }
        }, 1000)

    }

    override fun onBiometricAuthenticateError(error: Int, errMsg: String) {
        if (error ==  BiometricPrompt.ERROR_USER_CANCELED) {
            requireActivity().finish()
        }
    }

    override fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult) {
        // Start the animation to App's title
        binding.appName.startAnimation(appNameAnim)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.logoAnim.playAnimation()
        }, 500)

        Handler(Looper.getMainLooper()).postDelayed({
            val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
            this.findNavController().navigate(action)
        }, 2200)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}