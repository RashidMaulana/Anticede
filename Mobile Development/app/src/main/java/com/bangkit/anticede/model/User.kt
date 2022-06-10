package com.bangkit.anticede.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String? = null,
    var age: String? = null,
    var id: String? = null
) : Parcelable