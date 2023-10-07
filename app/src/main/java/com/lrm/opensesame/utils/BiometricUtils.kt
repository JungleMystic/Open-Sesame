package com.lrm.opensesame.utils

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object BiometricUtils {

    // Check whether the Device is Capable of the Biometric
    private fun hasBiometricCapability(context: Context): Int {
        return BiometricManager.from(context).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
    }

    fun isBiometricReady(context: Context) =
        hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS

    private fun setBiometricPromptInfo(
        title: String,
        subtitle: String,
        description: String
    ): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()
    }

    private fun initBiometricPrompt(
        fragment: Fragment,
        listener: BiometricAuthListener
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(fragment.requireActivity())
        val callback = object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener.onBiometricAuthenticateError(errorCode, errString.toString())
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.onBiometricAuthenticateSuccess(result)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        }
        return BiometricPrompt(fragment, executor, callback)
    }

    fun showBiometricPrompt(
        title: String = "Open Sesame",
        subtitle: String = "Confirm your screen lock or fingerprint",
        description: String = "It is required to Get in...",
        fragment: Fragment,
        listener: BiometricAuthListener,
        cryptoObject: BiometricPrompt.CryptoObject? = null
    ) {
        val promptInfo = setBiometricPromptInfo(title, subtitle, description)

        val biometricPrompt = initBiometricPrompt(fragment, listener)
        biometricPrompt.apply {
            if (cryptoObject == null) authenticate(promptInfo)
            else authenticate(promptInfo, cryptoObject)
        }
    }
}

interface BiometricAuthListener {

    fun onBiometricAuthenticateError(error: Int, errMsg: String)
    fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult)

}