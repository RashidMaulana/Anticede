package com.bangkit.anticede.ui.about

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.anticede.R
import com.bangkit.anticede.adapter.AboutAdapter
import com.bangkit.anticede.databinding.FragmentAboutBinding
import com.bangkit.anticede.model.TeamMember

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

        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        binding.rvMember.setHasFixedSize(true)
        list.addAll(listMember)
        showRecyclerList()
    }

    private val listMember: ArrayList<TeamMember>
        get() {
            val dataName = resources.getStringArray(R.array.name)
            val dataId = resources.getStringArray(R.array.id)
            val dataPhoto = resources.obtainTypedArray(R.array.photo)
            val listMember = ArrayList<TeamMember>()
            for(i in dataName.indices){
                val member = TeamMember(dataPhoto.getResourceId(i, -1), dataName[i], dataId[i])
                listMember.add(member)
            }
            dataPhoto.recycle()
            return listMember
        }

    private fun showRecyclerList(){
        val applicationContext = activity?.applicationContext
        if (applicationContext?.resources?.configuration!!.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvMember.layoutManager = GridLayoutManager(activity, 2)
        } else {
            binding.rvMember.layoutManager = LinearLayoutManager(activity)
        }
        val aboutAdapter = AboutAdapter(list)
        binding.rvMember.adapter = aboutAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}