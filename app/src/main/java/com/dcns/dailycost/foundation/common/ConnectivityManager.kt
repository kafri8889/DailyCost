package com.dcns.dailycost.foundation.common

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class ConnectivityManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val connectionLiveData = NetworkConnectivity(context)

    private val _isNetworkAvailable: MutableLiveData<Boolean?> = MutableLiveData(null)
    val isNetworkAvailable: LiveData<Boolean?> = _isNetworkAvailable

    fun initialize() {
        connectionLiveData.initialize()
    }

    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.observe(lifecycleOwner) { isConnected ->
            Timber.i("is connected: $isConnected")
            _isNetworkAvailable.value = isConnected
        }
    }

    fun unregisterConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.removeObservers(lifecycleOwner)
    }

}