package com.bangkit.anticede.ui.about

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.anticede.OnBoardingActivity
import com.bangkit.anticede.R
import com.bangkit.anticede.adapter.AboutAdapter
import com.bangkit.anticede.databinding.FragmentAboutBinding
import com.bangkit.anticede.model.TeamMember
import com.bangkit.anticede.preferences.user.PreferenceFactory
import com.bangkit.anticede.preferences.user.PreferenceViewModel
import com.bangkit.anticede.preferences.user.UserPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<TeamMember>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        val toolbar = binding.toolbar
        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(toolbar)
        activity.title = "About Us"


        binding.rvMember.setHasFixedSize(true)
        list.addAll(listMember)
        showRecyclerList()
    }

    private val listMember: ArrayList<TeamMember>
        get() {
            val dataName = resources.getStringArray(R.array.name)
            val dataId = resources.getStringArray(R.array.id)
            val dataPhoto = resources.obtainTypedArray(R.array.photo)
            val dataLinkedIn = resources.getStringArray(R.array.linkedin)
            val listMember = ArrayList<TeamMember>()
            for (i in dataName.indices) {
                val member = TeamMember(
                    dataPhoto.getResourceId(i, -1), dataName[i],
                    dataId[i], dataLinkedIn[i]
                )
                listMember.add(member)
            }
            dataPhoto.recycle()
            return listMember
        }

    private fun showRecyclerList() {
        val applicationContext = activity?.applicationContext
        if (applicationContext?.resources?.configuration!!.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvMember.layoutManager = GridLayoutManager(activity, 2)
        } else {
            binding.rvMember.layoutManager = LinearLayoutManager(activity)
        }
        val aboutAdapter = AboutAdapter(list)
        binding.rvMember.adapter = aboutAdapter

        aboutAdapter.setOnItemClickCallback(object : AboutAdapter.OnItemClickCallback {
            override fun onItemClicked(data: TeamMember) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(data.linkedin)
                startActivity(intent)
            }
        })
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
}