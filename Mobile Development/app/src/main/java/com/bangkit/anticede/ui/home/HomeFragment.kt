package com.bangkit.anticede.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bangkit.anticede.R
import com.bangkit.anticede.databinding.FragmentHomeBinding
import com.bangkit.anticede.preferences.user.PreferenceFactory
import com.bangkit.anticede.preferences.user.PreferenceViewModel
import com.bangkit.anticede.preferences.user.UserPreferences
import com.bangkit.anticede.utilities.Utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mMp: MediaPlayer? = null
    private var isReady: Boolean = false

    private var getFile: File? = null

    @SuppressLint("SimpleDateFormat")
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    private val mMr = MediaMetadataRetriever()

    private val getVoice =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val recordVoice = result.data?.data as Uri
                getFile = uriToFile(recordVoice, requireContext())
                Log.d("HomeFragment", "getFile: $getFile")

                val filePath = getFile?.absolutePath

                binding.recordingTitle.text = getFile?.name

                val fileDate = Date(getFile?.lastModified()!!)
                binding.recordingDate.text = dateFormatter.format(fileDate).toString()

                mMr.setDataSource(filePath)
                val duration =
                    mMr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                binding.recordingLength.text = durationFormat(duration!!)

                initMp(filePath)
            }
        }

    private val launcherIntentPick = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile

            val filePath = getFile?.absolutePath

            binding.recordingTitle.text = myFile.name

            val fileDate = Date(myFile.lastModified())
            binding.recordingDate.text = dateFormatter.format(fileDate).toString()

            mMr.setDataSource(filePath)
            val duration =
                mMr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
            binding.recordingLength.text = durationFormat(duration!!)

            initMp(filePath)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val homeViewModel by viewModels<HomeViewModel>()

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.responseMessage.observe(viewLifecycleOwner) {
            showText(it)
        }

        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val prefView = ViewModelProvider(this, PreferenceFactory(pref)).get(
            PreferenceViewModel::class.java
        )

        prefView.getTokenUserSession().observe(viewLifecycleOwner){
            Log.d("HomeFragment", "token: $it")
        }

        binding.tvWarn.movementMethod = ScrollingMovementMethod()

        val toolbar = binding.toolbar
        val activity = activity as AppCompatActivity?

        activity!!.setSupportActionBar(toolbar)
        activity.title = "Home"

        binding.imageButton2.setOnClickListener {
            startRecording()
            if (isReady) {
                mMp?.stop()
                isReady = false
            }
        }

        binding.imageButton3.setOnClickListener {
            startPick()
            if (isReady) {
                mMp?.stop()
                isReady = false
            }
        }

        binding.playBtn.setOnClickListener {
            if (mMp != null) {
                if (!isReady) {
                    mMp?.prepareAsync()
                } else {
                    if (mMp?.isPlaying as Boolean) {
                        mMp?.pause()
                        binding.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                    } else {
                        mMp?.start()
                        binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24)
                    }
                }
            } else {
                Toast.makeText(requireActivity(), getString(R.string.warning2), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.stopBtn.setOnClickListener {
            if (mMp != null) {
                if (mMp?.isPlaying as Boolean || isReady) {
                    mMp?.stop()
                    isReady = false
                    binding.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                }
            } else {
                Toast.makeText(requireActivity(), getString(R.string.warning2), Toast.LENGTH_SHORT)
                    .show()
            }
        }



        binding.button.setOnClickListener {
            val fileUpload = getFile
            if (fileUpload != null) {
                val requestVoiceFile = fileUpload.asRequestBody("audio/x-aac".toMediaTypeOrNull())
                val requestBody =
                    MultipartBody.Part.createFormData("audio", fileUpload.name, requestVoiceFile)
                homeViewModel.uploadVoice(requireContext(), requestBody)
            } else {
                Toast.makeText(
                    requireActivity(), getString(R.string.warning5),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (getFile != null) {
            binding.recordingTitle.text = getFile?.name

            val fileDate = Date(getFile?.lastModified()!!)
            binding.recordingDate.text = dateFormatter.format(fileDate).toString()

            val duration =
                mMr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
            binding.recordingLength.text = durationFormat(duration!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun startRecording() {
        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            getVoice.launch(intent)
        } else {
            Toast.makeText(requireActivity(), getString(R.string.warning6), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startPick() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "audio/*"
        val chooser = Intent.createChooser(intent, getString(R.string.chooseRecorder))
        launcherIntentPick.launch(chooser)
    }

    private fun initMp(filePath: String?) {
        if (mMp == null) {
            mMp = MediaPlayer()
        } else {
            mMp?.reset()
        }
        val attribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        mMp?.setAudioAttributes(attribute)

        if (filePath != null) {
            Log.d("HomeFragment", "filepath: $filePath")
            try {
                mMp?.setDataSource(filePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mMp?.setOnPreparedListener {
                isReady = true
                mMp?.start()
                binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24)
            }
        } else {
            isReady = false
        }
        mMp?.setOnCompletionListener { binding.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24) }

        mMp?.setOnErrorListener { _, _, _ -> false }
    }

    private fun durationFormat(milli: Long): String {
        var finalTimer = ""
        val secondTimer: String

        val hours: Long = (milli / (1000 * 60 * 60))
        val minutes = (milli % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milli % (1000 * 60 * 60) % (1000 * 60) / 1000)

        if (hours > 0) finalTimer = "$hours:"
        secondTimer = if (seconds < 10) {
            "0$seconds"
        } else {
            "$seconds"
        }
        finalTimer = "$finalTimer$minutes:$secondTimer"

        return finalTimer
    }

    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showText(text : String) {
        val homeViewModel by viewModels<HomeViewModel>()

        if(text == "null"){
            binding.tvWarn.visibility = View.GONE
        } else {
            homeViewModel.voiceTranscription.observe(viewLifecycleOwner){
                val stats = "\"$it\"\n\n$text"
                binding.tvWarn.text =  stats
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_button -> {
                val homeViewModel by viewModels<HomeViewModel>()

                homeViewModel.isLoading.observe(viewLifecycleOwner) {
                    showLoading(it)
                }

                val pref = UserPreferences.getInstance(requireContext().dataStore)
                val prefView = ViewModelProvider(this, PreferenceFactory(pref)).get(
                    PreferenceViewModel::class.java
                )

                prefView.saveUserSession("null")

                homeViewModel.logout(requireContext())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}