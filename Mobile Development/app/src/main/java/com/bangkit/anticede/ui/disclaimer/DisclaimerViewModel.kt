package com.bangkit.anticede.ui.disclaimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DisclaimerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is disclaimer Fragment"
    }
    val text: LiveData<String> = _text
}