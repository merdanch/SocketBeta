package tm.com.beletfilm.socketbeta

import android.util.Log
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
            .on(Socket.EVENT_CONNECT) {
                Log.e("SOCKET_EVENT", "CONNECTED")
            }
            .on(Socket.EVENT_DISCONNECT) {
                Log.e("SOCKET_EVENT", "CONNECTED")
            }
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}