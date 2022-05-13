package com.bangkit.anticede.ui.disclaimer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.anticede.BuildConfig
import com.bangkit.anticede.databinding.FragmentDisclaimerBinding

class DisclaimerFragment : Fragment() {

    private var _binding: FragmentDisclaimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDisclaimerBinding.inflate(inflater, container, false)
        binding.button2.setOnClickListener { view ->
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // only email apps should handle this
                putExtra(Intent.EXTRA_EMAIL, arrayOf(DEV_EMAIL))
                putExtra(Intent.EXTRA_SUBJECT, "Anticede Data Disclaimer Request Remove")
                putExtra(Intent.EXTRA_TEXT, "Mohon hapus data saya dari aplikasi Anticede")
            }
            val packageManager = requireContext().packageManager
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val DEV_EMAIL = BuildConfig.DEVELOPER_EMAIL
    }
}