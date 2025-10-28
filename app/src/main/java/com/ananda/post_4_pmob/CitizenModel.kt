package com.ananda.post_4_pmob

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citizen")
data class CitizenModel(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	
	val fullName: String,
	val nationalId: String,
	val province: String,
	val district: String,
	val village: String,
	val rt: Int,
	val rw: Int,
	val gender: String,
	val maritalStatus: String
)