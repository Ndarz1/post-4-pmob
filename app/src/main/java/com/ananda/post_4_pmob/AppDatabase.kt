package com.ananda.post_4_pmob

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CitizenModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
	abstract fun citizenDao(): CitizenDao
}
