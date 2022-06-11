package com.bangkit.anticede.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminLoginResponse(

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("message")
    val message: String
) : Parcelable
