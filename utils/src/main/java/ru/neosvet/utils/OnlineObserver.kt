package geekbrains.ru.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.neosvet.utils.R
import java.io.IOException

class OnlineObserver(context: Context) {
    private var lastValue = false
    private val _isOnline = MutableStateFlow<Boolean>(lastValue)
    val isOnline: StateFlow<Boolean> = _isOnline
    val exception = IOException(context.getString(R.string.no_connection))

    private val availableNetworks = mutableSetOf<Network>()
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val request: NetworkRequest = NetworkRequest.Builder().build()
    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            availableNetworks.remove(network)
            update(availableNetworks.isNotEmpty())
        }

        override fun onAvailable(network: Network) {
            availableNetworks.add(network)
            update(availableNetworks.isNotEmpty())
        }
    }

    init{
        onActive()
    }

    fun onActive() {
        connectivityManager.registerNetworkCallback(request, callback)
    }

    fun onInactive() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

    private fun update(online: Boolean) {
        if (online != lastValue) {
            lastValue = online
            _isOnline.tryEmit(online)
        }
    }
}