package com.bangkit.anticede.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.anticede.R
import com.bangkit.anticede.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.editTextTextUserName.doOnTextChanged { text, _, _, _ ->
            if (text?.length!! < 6) {
                binding.editTextTextUserName.error = getString(R.string.username_warning)
            } else {
                binding.editTextTextUserName.error = null
            }
        }
        binding.editTextTextPassword.doOnTextChanged { text, _, _, _ ->
            if (text?.length!! < 6) {
                binding.editTextTextPassword.error = getString(R.string.password_warning2)
            } else {
                binding.editTextTextPassword.error = null
            }
        }
        binding.editTextTextConfirmationPassword.doOnTextChanged { text, _, _, _ ->
            if (text.contentEquals(binding.editTextTextPassword.text)) {
                binding.editTextTextConfirmationPassword.error = null
            } else {
                binding.editTextTextConfirmationPassword.error =
                    getString(R.string.password_warning)
            }
        }

        registerUser()
    }

    private fun registerUser() {
        binding.button3.setOnClickListener {
            val username = binding.editTextTextUserName.text.trim().toString()
            val age = binding.editTextNumber.text.trim().toString()
            val password = binding.editTextTextPassword.text.trim().toString()
            val confirmPassword = binding.editTextTextConfirmationPassword.text.trim().toString()

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
                age.isEmpty() -> {
                    binding.editTextNumber.error = resources.getString(R.string.warning_empty_age)
                    binding.editTextNumber.requestFocus()
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
                confirmPassword.isEmpty() -> {
                    binding.editTextTextPassword.error =
                        resources.getString(R.string.warning_empty_password)
                    binding.editTextTextPassword.requestFocus()
                    return@setOnClickListener
                }
                confirmPassword != password -> {
                    binding.editTextTextConfirmationPassword.error =
                        getString(R.string.password_warning)
                    binding.editTextTextUserName.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    registerViewModel.registerUser(requireContext(), username, age, confirmPassword)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }
}