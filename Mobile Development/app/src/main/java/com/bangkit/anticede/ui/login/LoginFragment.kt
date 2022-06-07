package com.bangkit.anticede.ui.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.bangkit.anticede.R
import com.bangkit.anticede.databinding.FragmentLoginBinding
import com.bangkit.anticede.preferences.user.PreferenceFactory
import com.bangkit.anticede.preferences.user.PreferenceViewModel
import com.bangkit.anticede.preferences.user.UserPreferences
import com.bangkit.anticede.ui.admin.login.AdminLoginActivity

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
                Toast.makeText(requireContext(),getString(R.string.warning_empty_login), Toast.LENGTH_SHORT).show()
            } else{
                loginViewModel.loginUser(requireContext(), binding.editTextTextUserName.text.toString(), binding.editTextTextPassword.text.toString())
            }
        }

        merubahText()
    }

    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }

    fun merubahText() {

        //Membuat teksnya
        val spannableString = SpannableString(getString(R.string.text_login_admin))

        //Menentukan teks dapat diklik dan method apa yang akan dijalankan
        val clickableSpan = object : ClickableSpan() {
            //Method yang akan dijalankan saat diklik, mengganti layar activity
            override fun onClick(view: View) {
                startActivity(Intent(requireContext(), AdminLoginActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                //merubah warna teks yang dapat diklik dan menghilangkan garis bawah
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#F52121")
                ds.isUnderlineText = false
            }

        }
        //Mengaplikasikan teks yang dapat diklik
        spannableString.setSpan(clickableSpan, 19, 24, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        //Mengganti teks pada textview agar dapat diklik
        binding.toLoginAdmin.text = spannableString

        //merubah movement method
        binding.toLoginAdmin.movementMethod = LinkMovementMethod.getInstance()
    }

}