package tm.com.beletfilm.socketbeta

import android.util.Log
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("http://45.93.136.141:5004?phone=64838630")

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