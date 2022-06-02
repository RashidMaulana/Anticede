package com.bangkit.anticede.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bangkit.anticede.databinding.FragmentLoginBinding
import com.bangkit.anticede.preferences.PreferenceFactory
import com.bangkit.anticede.preferences.PreferenceViewModel
import com.bangkit.anticede.preferences.UserPreferences
import com.bangkit.anticede.ui.home.HomeViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginViewModel by viewModels<LoginViewModel>()

        loginViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }



        binding.button3.setOnClickListener {
            val pref = UserPreferences.getInstance(requireContext().dataStore)
            val prefView = ViewModelProvider(this, PreferenceFactory(pref)).get(
                PreferenceViewModel::class.java
            )

            loginViewModel.getUser().observe(viewLifecycleOwner) {
                prefView.saveUserSession(it.userId)
            }

            if(binding.editTextTextPassword.text.isNullOrBlank() || binding.editTextTextUserName.text.isNullOrBlank()){
                Toast.makeText(requireContext(),"Isikan form dengan benar!", Toast.LENGTH_SHORT).show()
            } else{
                loginViewModel.loginUser(requireContext(), binding.editTextTextUserName.text.toString(), binding.editTextTextPassword.text.toString())
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