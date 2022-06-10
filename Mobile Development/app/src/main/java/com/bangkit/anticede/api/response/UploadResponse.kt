package com.bangkit.anticede.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadResponse(

	@field:SerializedName("transcription")
	val transcription: String,

	@field:SerializedName("message")
	val message: String
) : Parcelable
