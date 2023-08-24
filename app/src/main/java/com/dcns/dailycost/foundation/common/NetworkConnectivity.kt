package com.dcns.dailycost.foundation.common

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class NetworkConnectivity(
	private val context: Context
): LiveData<Boolean>() {

	private lateinit var networkCallback: ConnectivityManager.NetworkCallback
	private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
	private val validNetworks: MutableSet<Network> = HashSet()

	fun initialize() {
		checkValidNetworks()
		// Assume the device is now online
//        postValue(true)
	}

	private fun checkValidNetworks() {
		Timber.i("has internet? -> ${validNetworks.size > 0}")
		postValue(validNetworks.size > 0)
	}

	override fun onActive() {
		networkCallback = createNetworkCallback()
		val networkRequest = NetworkRequest.Builder()
			.addCapability(NET_CAPABILITY_INTERNET)
			.build()
		cm.registerNetworkCallback(networkRequest, networkCallback)
	}

	override fun onInactive() {
		cm.unregisterNetworkCallback(networkCallback)
	}

	private fun createNetworkCallback() = object: ConnectivityManager.NetworkCallback() {

		/*
		  Called when a network is detected. If that network has internet, save it in the Set.
		  Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onAvailable(android.net.Network)
		 */
		override fun onAvailable(network: Network) {
			val networkCapabilities = cm.getNetworkCapabilities(network)
			val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)

			if (hasInternetCapability == true) {
				// check if this network actually has internet
				CoroutineScope(Dispatchers.IO).launch {
					val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
					if (hasInternet) {
						withContext(Dispatchers.Main) {

							validNetworks.add(network)
							checkValidNetworks()
						}
					}
				}
			}
		}

		/*
		  If the callback was registered with registerNetworkCallback() it will be called for each network which no longer satisfies the criteria of the callback.
		  Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onLost(android.net.Network)
		 */
		override fun onLost(network: Network) {
			Timber.i("onLost: $network")
			validNetworks.remove(network)
			checkValidNetworks()
		}

	}

}