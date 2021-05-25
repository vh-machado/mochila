package com.example.mochila.principal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mochila.R
import com.example.mochila.principal.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    val scope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashCoroutines()
    }
    fun splashCoroutines(){
        val intent = Intent(this,LoginActivity::class.java)
        scope.launch{
            delay(3000)
            startActivity(intent)
            finish()
        }
    }
}