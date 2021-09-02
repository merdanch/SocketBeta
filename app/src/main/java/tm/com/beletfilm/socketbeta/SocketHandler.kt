package tm.com.beletfilm.socketbeta

import android.util.Log
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(ip:String, phone:String) {
        try {
            val uri = "http://$ip?phone=$phone"

            mSocket = IO.socket(uri)

            Log.e("setSocket uri", uri)
            Log.e("setSocket ip", ip)
            Log.e("setSocket phone", phone)

        } catch (e: URISyntaxException) {
            Log.e("setSocket", e.toString())
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {

        Log.e("SOCKET INFO", "establishConnection")

        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}