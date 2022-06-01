package com.bangkit.anticede.ui.disclaimer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bangkit.anticede.BuildConfig
import com.bangkit.anticede.OnBoardingActivity
import com.bangkit.anticede.R
import com.bangkit.anticede.databinding.FragmentDisclaimerBinding
import com.bangkit.anticede.preferences.PreferenceFactory
import com.bangkit.anticede.preferences.PreferenceViewModel
import com.bangkit.anticede.preferences.UserPreferences
import com.bangkit.anticede.ui.home.HomeViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_button -> {
                val pref = UserPreferences.getInstance(requireContext().dataStore)
                val prefView = ViewModelProvider(this, PreferenceFactory(pref)).get(
                    PreferenceViewModel::class.java
                )

                prefView.saveUserSession("null")
                val intentToOnboard = Intent(requireContext(), OnBoardingActivity::class.java)
                intentToOnboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intentToOnboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intentToOnboard)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val DEV_EMAIL = BuildConfig.DEVELOPER_EMAIL
    }
}