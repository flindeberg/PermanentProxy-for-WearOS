package nl.jolanrensen.permanentproxy

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import kotlin.concurrent.thread

object Constants {
    const val LOGTAG = "PermanentProxy"

    fun logD(message: String) = Log.d(LOGTAG, message)
    fun logI(message: String) = Log.i(LOGTAG, message)
    fun logE(message: String, e: java.lang.Exception? = null) = Log.e(LOGTAG, message, e)

    fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, length).show()

    fun Context.toastLong(message: String) = toast(message, Toast.LENGTH_LONG)

    fun getTurnOnProxyCommand(address: String, port: Int) =
        "settings put global http_proxy $address:$port\n"

    fun getTurnOffProxyCommand() =
        "settings delete global http_proxy; settings delete global global_http_proxy_host; settings delete global global_http_proxy_port"

    fun Context.startProxy(
        address: String,
        port: Int,
        updateGooglePay: Boolean = true
    ) {

        Settings.Global.putString(contentResolver, Settings.Global.HTTP_PROXY, "$address:$port")
        if (updateGooglePay) sendBroadcast(
            Intent("android.server.checkin.CHECKIN")
        )
    }

    fun Context.stopProxy() {
        Settings.Global.putString(contentResolver, Settings.Global.HTTP_PROXY, null)
        Settings.Global.putString(contentResolver, "global_http_proxy_host", null)
        Settings.Global.putString(contentResolver, "global_http_proxy_port", null)
    }

    fun Context.getCurrentProxy() = try {
        Settings.Global.getString(contentResolver, Settings.Global.HTTP_PROXY)
    } catch (e: Exception) {
        logE("proxy", e)
        null
    }

}