package com.bangkit.anticede

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bangkit.anticede.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

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


        binding.editTextTextConfirmationPassword.doOnTextChanged { text, _, _, _ ->
            if (text.contentEquals(binding.editTextTextPassword.text)) {
                binding.editTextTextConfirmationPassword.error = null
            } else {
                binding.editTextTextConfirmationPassword.error =
                    getString(R.string.password_warning)
            }
        }

        binding.editTextTextUserName.doOnTextChanged { text, _, _, _ ->
            if (text?.length!! < 6) {
                binding.editTextTextUserName.error = getString(R.string.username_warning)
            } else {
                binding.editTextTextUserName.error = null
            }
        }

        binding.button3.setOnClickListener {
            val intent = Intent(activity, BottomNavigationActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}