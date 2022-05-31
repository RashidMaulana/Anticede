package com.bangkit.anticede.ui.disclaimer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bangkit.anticede.BuildConfig
import com.bangkit.anticede.R
import com.bangkit.anticede.databinding.FragmentDisclaimerBinding

class DisclaimerFragment : Fragment() {

    private var _binding: FragmentDisclaimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisclaimerBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(toolbar)
        activity.title = "Disclaimer"

        binding.button2.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // only email apps should handle this
                putExtra(Intent.EXTRA_EMAIL, arrayOf(DEV_EMAIL))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.warning3))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.warning4))
            }
            val packageManager = requireContext().packageManager
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        private const val DEV_EMAIL = BuildConfig.DEVELOPER_EMAIL
    }
}