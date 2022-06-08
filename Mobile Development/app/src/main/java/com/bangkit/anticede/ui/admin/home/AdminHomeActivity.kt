package com.bangkit.anticede.ui.admin.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.anticede.OnBoardingActivity
import com.bangkit.anticede.R
import com.bangkit.anticede.adapter.AdminAdapter
import com.bangkit.anticede.api.response.GetAllUserResponseItem
import com.bangkit.anticede.databinding.ActivityAdminHomeBinding
import com.bangkit.anticede.model.User
import com.bangkit.anticede.preferences.admin.AdminPreference
import com.bangkit.anticede.preferences.admin.AdminPreferenceFactory
import com.bangkit.anticede.preferences.admin.AdminPreferenceViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "AdminSession")
class AdminHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomeBinding

    private val adminLoginViewModel by viewModels<AdminHomeViewModel>()
    private lateinit var adminAdapter: AdminAdapter
    private var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Admin Home"

        adminLoginViewModel.isLoading.observe(this){
            showLoading(it)
        }
        adminLoginViewModel.getUsers(this)

        adminLoginViewModel.getListUser().observe(this){
            setStoryData(it)
        }
    }

    private fun setStoryData(items: List<GetAllUserResponseItem>) {
        for (user in items) {
            val new = User(user.username, user.age.toString(), user.id)
            userList.add(new)
        }
        adminAdapter = AdminAdapter(userList)
        binding.rvUser.adapter = adminAdapter
        binding.rvUser.layoutManager = LinearLayoutManager(this)

        adminAdapter.setOnItemClickCallback(object : AdminAdapter.OnItemClickCallback {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(user: User) {
                val idCatch = catchUserId(user)
                AlertDialog.Builder(this@AdminHomeActivity).apply {
                    setTitle(resources.getString(R.string.confirmationTitle) + " ${idCatch?.username}")
                    setMessage(resources.getString(R.string.confirmDelete))
                    setPositiveButton("Ya") { _: DialogInterface, _: Int ->
                        userList.remove(idCatch)
                        adminAdapter.notifyDataSetChanged()
                        adminLoginViewModel.deleteUser(this@AdminHomeActivity, idCatch?.id.toString())
                    }
                    setNeutralButton("Tidak") { _: DialogInterface, _: Int ->
                    }
                    create()
                    show()
                }
            }
        })
    }

    private fun catchUserId(id: User): User? {
        var select: User? = null

        for(user in userList) {
            if(user.username == id.username)
                select = user
        }

        return select
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.logout_button -> {
                val pref = AdminPreference.getInstance(dataStore)
                val prefView = ViewModelProvider(this, AdminPreferenceFactory(pref)).get(
                    AdminPreferenceViewModel::class.java
                )
                prefView.saveUserSession("null")
                val adminLoginViewModel by viewModels<AdminHomeViewModel>()
                adminLoginViewModel.logoutAdmin(this)

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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }
}