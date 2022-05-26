package com.bangkit.anticede.api.response

import com.google.gson.annotations.SerializedName


data class UploadResponse(
    @field:SerializedName("message")
    val message: String
)
