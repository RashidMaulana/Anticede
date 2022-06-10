package com.bangkit.anticede.ui.admin.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.anticede.R
import com.bangkit.anticede.databinding.ActivityAdminLoginBinding
import com.bangkit.anticede.preferences.admin.AdminPreference
import com.bangkit.anticede.preferences.admin.AdminPreferenceFactory
import com.bangkit.anticede.preferences.admin.AdminPreferenceViewModel
import com.bangkit.anticede.ui.admin.home.AdminHomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "AdminSession")

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adminLoginViewModel by viewModels<AdminLoginViewModel>()

        adminLoginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val pref = AdminPreference.getInstance(dataStore)
        val prefView = ViewModelProvider(this, AdminPreferenceFactory(pref)).get(
            AdminPreferenceViewModel::class.java
        )

        prefView.getTokenUserSession().observe(this) {
            if (it != "null") {
                val intent = Intent(this, AdminHomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        adminLoginViewModel.getAdmin().observe(this) {
            prefView.saveUserSession(it.userId)
        }

        binding.button3.setOnClickListener {
            val username = binding.editTextTextUserName.text.trim().toString()
            val password = binding.editTextTextPassword.text.trim().toString()

            when {
                username.isEmpty() -> {
                    binding.editTextTextUserName.error = getString(R.string.empty_username_warning)
                    binding.editTextTextUserName.requestFocus()
                    return@setOnClickListener
                }
                username.length < 6 -> {
                    binding.editTextTextUserName.error = getString(R.string.username_warning)
                    binding.editTextTextUserName.requestFocus()
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.editTextTextPassword.error =
                        resources.getString(R.string.warning_empty_password)
                    binding.editTextTextPassword.requestFocus()
                    return@setOnClickListener
                }
                password.length < 6 -> {
                    binding.editTextTextPassword.error = getString(R.string.username_warning)
                    binding.editTextTextUserName.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    adminLoginViewModel.loginAdmin(this, username, password)
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }
}