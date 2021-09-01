package tm.com.beletfilm.socketbeta.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import tm.com.beletfilm.socketbeta.R
import android.app.Activity

import android.content.Intent




class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsBackBtn.setOnClickListener {
            onBackPressed()
        }

        settingsSaveBtn.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("result","MICK")
            setResult(RESULT_OK, returnIntent)
            finish()
        }

    }
}