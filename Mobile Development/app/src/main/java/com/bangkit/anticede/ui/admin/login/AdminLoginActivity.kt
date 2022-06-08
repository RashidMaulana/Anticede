package com.bangkit.anticede.ui.admin.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
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

        adminLoginViewModel.getAdmin().observe(this){
            prefView.saveUserSession(it.userId)
        }

        binding.button3.setOnClickListener {
            if(binding.editTextTextPassword.text.isNullOrBlank() || binding.editTextTextUserName.text.isNullOrBlank()){
                Toast.makeText(this,getString(R.string.warning_empty_login), Toast.LENGTH_SHORT).show()
            } else{
                adminLoginViewModel.loginAdmin(this, binding.editTextTextUserName.text.toString(), binding.editTextTextPassword.text.toString())
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