package com.bangkit.anticede.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamMember(
    var photo: Int,
    var name: String,
    var id: String,
    var linkedin: String
) : Parcelable