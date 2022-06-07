package com.bangkit.anticede

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.anticede.adapter.SectionsPagerAdapter
import com.bangkit.anticede.databinding.ActivityOnBoardingBinding
import com.bangkit.anticede.preferences.user.PreferenceFactory
import com.bangkit.anticede.preferences.user.PreferenceViewModel
import com.bangkit.anticede.preferences.user.UserPreferences
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager

        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        tabs.setTabTextColors(
            ContextCompat.getColor(this, R.color.grey),
            ContextCompat.getColor(this, R.color.red_200)
        )

        supportActionBar?.hide()

        val pref = UserPreferences.getInstance(dataStore)
        val prefview = ViewModelProvider(this, PreferenceFactory(pref)).get(
            PreferenceViewModel::class.java
        )

        prefview.getTokenUserSession().observe(this) {
            if (it != "null") {
                val intentDetail = Intent(this, BottomNavigationActivity::class.java)
                startActivity(intentDetail)
                finish()
            }
        }
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )

    }
}