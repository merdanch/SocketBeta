package tm.com.beletfilm.socketbeta

import android.Manifest
import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import tm.com.beletfilm.socketbeta.main.MainViewModel
import tm.com.beletfilm.socketbeta.model.ConnectionStatus
import tm.com.beletfilm.socketbeta.settings.SettingsActivity
import java.lang.Exception


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        requestSmsPermission()

        viewModel.prepSocket()

        viewModel.isNewMessage.observe(this, Observer {
            if (it==true && viewModel.sendSms.value!=null)
                sendSMS(viewModel.sendSms.value!!.phone, viewModel.sendSms.value!!.message)
        })

        viewModel.connectionStatus.observe(this, Observer {
            when (it) {
                ConnectionStatus.CONNECTING -> {
                    homeProgressBar.isVisible = true
                    homeLottieAnim.isVisible = false
                    homeConnectionStatus.text = "CONNECTING"
                }
                ConnectionStatus.CONNECTED -> {
                    homeLottieAnim.isVisible = true
                    homeLottieAnim.setAnimation(R.raw.scan)
                    homeProgressBar.isVisible = false
                    homeConnectionStatus.text = "CONNECTED"
                }
                ConnectionStatus.DISCONNECTED -> {
                    homeLottieAnim.isVisible = true
                    homeLottieAnim.setAnimation(R.raw.disconnected)
                    homeConnectionStatus.text = "DISCONNECTED"
                    homeProgressBar.isVisible = false
                }
                ConnectionStatus.CONNECTION_ERROR -> {
                    homeLottieAnim.isVisible = true
                    homeLottieAnim.setAnimation(R.raw.disconnected)
                    homeProgressBar.isVisible = false
                    homeConnectionStatus.text = "CONNECTION_ERROR"
                }
            }
        })

        homeSettingsBtn.setOnClickListener {
            openSettingsForResult()
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

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)

    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val data: Intent? = result.data

                val result = data?.getStringExtra("result")

                Log.e("resultLauncher", result.toString())

            }
        }

    fun openSettingsForResult() {
        val intent = Intent(this, SettingsActivity::class.java)
        resultLauncher.launch(intent)
    }
}