package tm.com.beletfilm.socketbeta.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import tm.com.beletfilm.socketbeta.R
import android.app.Activity

import android.content.Intent
import android.text.Editable
import android.widget.Toast


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsBackBtn.setOnClickListener {
            onBackPressed()
        }

        settingsSaveBtn.setOnClickListener {
            if (!settingsIpEditText.text.isNullOrEmpty() && !settingsPhoneEditText.text.isNullOrEmpty()){
                val returnIntent = Intent()
                returnIntent.putExtra("ip",settingsIpEditText.text.toString())
                returnIntent.putExtra("phone",settingsPhoneEditText.text.toString())
                setResult(RESULT_OK, returnIntent)
                finish()
            }else{
                Toast.makeText(this,"Please fill out", Toast.LENGTH_SHORT).show()
            }
        }

        val prefs = getSharedPreferences("mySharedPref", MODE_PRIVATE)
        val ip = prefs.getString("ip", "BOSH")
        val phone = prefs.getString("phone", "BOSH")

        if (ip!="BOSH" && phone!="BOSH"){
            settingsIpEditText.setText(ip)
            settingsPhoneEditText.setText(phone)
        }

    }
}