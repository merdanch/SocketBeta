package tm.com.beletfilm.socketbeta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        val mSocket = SocketHandler.getSocket()


        val gson: Gson = Gson()

        mSocket.on("message") { args ->
            if (args[0] != null) {
                val a = gson.toJson(args[0])
                val res = gson.fromJson(a, nameValuePairs::class.java)

                val phone = res.nameValuePairs.phone
                val message = res.nameValuePairs.message


                runOnUiThread {
                    Log.e("SOCKET EVENT MESSAGE", phone)
                    Log.e("SOCKET EVENT MESSAGE", message)
                }
            }
        }

//        mSocket.emit("success")

    }
}