package com.dcns.dailycost.foundation.common

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import timber.log.Timber

class DailyCostBiometricManager(private val activity: FragmentActivity) {

    private var listener: BiometricListener? = null

    fun setListener(listener: BiometricListener) {
        this.listener = listener
    }

    fun createPromptInfo(
        title: String,
        subtitle: String,
        description: String,
        negativeButtonText: String = "Cancel",
        allowedAuthenticators: Int = -1
    ): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(title)
            setSubtitle(subtitle)
            setDescription(description)

            // allowedAuthenticators: DEVICE_CREDENTIAL or BIOMETRIC_WEAK or BIOMETRIC_STRONG
            if (allowedAuthenticators != -1) setAllowedAuthenticators(allowedAuthenticators)

            setNegativeButtonText(negativeButtonText)
        }.build()
    }

    fun createBiometricPrompt(
        onSuccess: () -> Unit = {}
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        // Callback
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Timber.e("authentication error: $errorCode | $errString")
                listener?.onError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Timber.e("authentication success")
                listener?.onSuccess(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Timber.e("authentication failed: unknown")
                listener?.onFailed()
            }
        }

        return BiometricPrompt(activity, executor, callback)
    }

    fun showAuthentication(
        title: String,
        subtitle: String,
        description: String,
        negativeButtonText: String = "Cancel",
        allowedAuthenticators: Int = -1,
        onSuccess: () -> Unit = {}
    ) {
        createBiometricPrompt(
            onSuccess = onSuccess
        ).authenticate(
            createPromptInfo(
                title = title,
                subtitle = subtitle,
                description = description,
                negativeButtonText = negativeButtonText,
                allowedAuthenticators = allowedAuthenticators
            )
        )
    }

    interface BiometricListener {

        fun onSuccess(result: BiometricPrompt.AuthenticationResult)

        fun onError(errorCode: Int, errString: CharSequence)

        fun onFailed()
    }

    companion object {
        fun hasBiometric(context: Context, authenticators: Int = BiometricManager.Authenticators.DEVICE_CREDENTIAL): Int {
            return BiometricManager.from(context).canAuthenticate(authenticators)
        }

        fun canAuthenticateWithAuthenticators(
            context: Context,
            authenticators: Int = BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ): Boolean {
            return hasBiometric(context, authenticators) == BiometricManager.BIOMETRIC_SUCCESS
        }
    }

}

val LocalBiometricHandler: ProvidableCompositionLocal<DailyCostBiometricManager?> = compositionLocalOf { null }
