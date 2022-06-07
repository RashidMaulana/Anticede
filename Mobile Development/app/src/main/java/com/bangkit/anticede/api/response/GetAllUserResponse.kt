package com.bangkit.anticede.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAllUserResponse(

	@field:SerializedName("GetAllUserResponse")
	val getAllUserResponse: List<GetAllUserResponseItem>
): Parcelable

@Parcelize
data class GetAllUserResponseItem(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("username")
	val username: String
): Parcelable
