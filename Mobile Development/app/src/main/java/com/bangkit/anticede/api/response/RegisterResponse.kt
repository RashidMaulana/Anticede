package com.bangkit.anticede.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
) : Parcelable

@Parcelize
data class Data(
    @field:SerializedName("userId")
    val userId: String
) : Parcelable
