package tm.com.beletfilm.socketbeta

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import com.google.gson.Gson
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat
import android.widget.Toast
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestSmsPermission()

        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        val mSocket = SocketHandler.getSocket()


        val gson = Gson()

        mSocket.on("message") { args ->
            if (args[0] != null) {
                val a = gson.toJson(args[0])
                val res = gson.fromJson(a, nameValuePairs::class.java)

                val phone = res.nameValuePairs.phone
                val message = res.nameValuePairs.message


                runOnUiThread {
                    Log.e("SOCKET EVENT MESSAGE", phone)
                    Log.e("SOCKET EVENT MESSAGE", message)

                    sendSMS("+993$phone", message)

                    mSocket.emit("success")
                }
            }
        }

        mSocket.on(Socket.EVENT_CONNECT) {
            Log.e("SOCKET_EVENT", "CONNECTED")
            Toast.makeText(this,"CONNECTED", Toast.LENGTH_SHORT).show()
        }.on(Socket.EVENT_DISCONNECT) {
            Log.e("SOCKET_EVENT", "DISCONNECTED")
            Toast.makeText(this,"DISCONNECTED", Toast.LENGTH_SHORT).show()
        }

    }

    private val PERMISSION_SEND_SMS = 123


    private fun requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // request permission (see result in onRequestPermissionsResult() method)
            Log.e("Permission", "ASKED")
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.SEND_SMS),
                PERMISSION_SEND_SMS
            )
        }
    }

    private fun sendSMS(phoneNumber: String, message: String) {

        Log.e("SEND SMS", "BARDEE")

        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(
                applicationContext, "Message Sent",
                Toast.LENGTH_LONG
            ).show()
        } catch (ex: Exception) {
            Toast.makeText(
                applicationContext, ex.message.toString(),
                Toast.LENGTH_LONG
            ).show()
            ex.printStackTrace()
        }
    }
}