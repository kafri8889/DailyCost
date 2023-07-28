package com.dcns.dailycost.foundation.common

import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.SocketFactory

/**
 * Send a ping to googles primary DNS.
 * If successful, that means we have internet.
 */
object DoesNetworkHaveInternet {

    // Make sure to execute this on a background thread.
    fun execute(socketFactory: SocketFactory): Boolean {
        return try{
            Timber.d("PINGING google.")
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Timber.d("PING success.")
            true
        } catch (e: IOException){
            Timber.e(e, "No internet connection")
            false
        } catch (e: SocketTimeoutException) {
            Timber.e(e, "Socket timeout")
            false
        } catch (e: UnknownHostException) {
            Timber.e(e, "Unknown host")
            false
        }
    }
}