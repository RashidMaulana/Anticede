package com.bangkit.anticede.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bangkit.anticede.BottomNavigationActivity
import com.bangkit.anticede.databinding.FragmentHomeBinding
import com.bangkit.anticede.utilities.Utils.createTempRecordFile
import java.io.File


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var getFile: File? = null
    private lateinit var currentRecordPath: String

    private val getVoice =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val myFile = File(currentRecordPath)
                getFile = myFile
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar: ActionBar? = (activity as BottomNavigationActivity).supportActionBar
        actionBar?.setTitleColor(Color.BLACK)

        binding.imageButton2.setOnClickListener {
            startRecording()
            Log.d("HomeFragment", "${getFile}")
            Log.d("HomeFragment", "${getFile?.absolutePath}")
            Log.d("HomeFragment", currentRecordPath)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun ActionBar.setTitleColor(color: Int) {
        val text = SpannableString(title ?: "")
        text.setSpan(ForegroundColorSpan(color),0,text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        title = text
    }

    private fun startRecording() {
        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        intent.resolveActivity(requireActivity().packageManager)

        createTempRecordFile(requireActivity().application).also {
            val recordURI : Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.bangkit.anticede",
                it
            )
            currentRecordPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, recordURI)
            getVoice.launch(intent)
        }
    }
}