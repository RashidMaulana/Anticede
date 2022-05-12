package com.bangkit.anticede

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar?.hide()

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000L)
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)


            finish()
        }
    }
}