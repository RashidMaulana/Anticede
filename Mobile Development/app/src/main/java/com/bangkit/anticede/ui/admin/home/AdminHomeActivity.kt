package com.bangkit.anticede.ui.admin.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.anticede.OnBoardingActivity
import com.bangkit.anticede.R
import com.bangkit.anticede.databinding.ActivityAdminHomeBinding
import com.bangkit.anticede.preferences.admin.AdminPreference
import com.bangkit.anticede.preferences.admin.AdminPreferenceFactory
import com.bangkit.anticede.preferences.admin.AdminPreferenceViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "AdminSession")
class AdminHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Admin Home"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.logout_button -> {
                val pref = AdminPreference.getInstance(dataStore)
                val prefview = ViewModelProvider(this, AdminPreferenceFactory(pref)).get(
                    AdminPreferenceViewModel::class.java
                )
                prefview.saveUserSession("null")
                val intentToMain = Intent(this, OnBoardingActivity::class.java)
                intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intentToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intentToMain)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
}