package com.ananda.post_4_pmob

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CitizenDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertCitizen(citizen: CitizenModel)
	
	@Query("SELECT * FROM citizen ORDER BY id DESC")
	suspend fun getAllCitizens(): List<CitizenModel>
	
	@Query("DELETE FROM citizen")
	suspend fun deleteAllCitizens()
}

