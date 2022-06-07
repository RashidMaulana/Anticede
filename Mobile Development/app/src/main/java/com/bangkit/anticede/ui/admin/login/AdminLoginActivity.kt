package com.bangkit.anticede.ui.admin.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.anticede.databinding.ActivityAdminLoginBinding

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}