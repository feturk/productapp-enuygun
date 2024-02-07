package com.feyzaeda.productapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feyzaeda.productapp.databinding.ActivitySplashBinding
import com.feyzaeda.productapp.util.Constants.SPLASH_TIMEOUT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var splashBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)


        Thread {
            try {
                Thread.sleep(SPLASH_TIMEOUT)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                 val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()

    }
}