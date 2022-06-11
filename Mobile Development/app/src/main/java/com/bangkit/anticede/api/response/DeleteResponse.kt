package com.bangkit.anticede.api.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeleteResponse(
    val data: List<DataItem>,
    val message: String
) : Parcelable

@Parcelize
data class DataItem(
    val password: String,
    val id: String,
    val age: Int,
    val username: String
) : Parcelable
