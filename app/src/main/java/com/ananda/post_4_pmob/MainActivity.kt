package com.ananda.post_4_pmob


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
	
	private lateinit var db: AppDatabase
	private lateinit var citizenAdapter: CitizenAdapter
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		db = Room.databaseBuilder(
			applicationContext,
			AppDatabase::class.java,
			"citizen_db"
		).build()
		
		val fullNameInput = findViewById<EditText>(R.id.etNamaLengkap)
		val nationalIdInput = findViewById<EditText>(R.id.etNIK)
		val provinceInput = findViewById<EditText>(R.id.etKabupaten)
		val districtInput = findViewById<EditText>(R.id.etKecamatan)
		val villageInput = findViewById<EditText>(R.id.etDesa)
		val rtInput = findViewById<EditText>(R.id.etRT)
		val rwInput = findViewById<EditText>(R.id.etRW)
		val genderGroup = findViewById<RadioGroup>(R.id.rgGender)
		val maritalStatusSpinner = findViewById<Spinner>(R.id.spStatus)
		val saveButton = findViewById<Button>(R.id.btnSimpan)
		val resetButton = findViewById<Button>(R.id.btnReset)
		val citizenRecyclerView = findViewById<RecyclerView>(R.id.rvPenduduk)
		
		citizenAdapter = CitizenAdapter(listOf())
		citizenRecyclerView.layoutManager = LinearLayoutManager(this)
		citizenRecyclerView.adapter = citizenAdapter
		
		saveButton.setOnClickListener {
			val gender = when (genderGroup.checkedRadioButtonId) {
				R.id.rbLaki -> "Laki-Laki"
				R.id.rbPerempuan -> "Perempuan"
				else -> "-"
			}
			
			val citizen = CitizenModel(
				fullName = fullNameInput.text.toString(),
				nationalId = nationalIdInput.text.toString(),
				province = provinceInput.text.toString(),
				district = districtInput.text.toString(),
				village = villageInput.text.toString(),
				rt = rtInput.text.toString(),
				rw = rwInput.text.toString(),
				gender = gender,
				maritalStatus = maritalStatusSpinner.selectedItem.toString()
			)
			
			lifecycleScope.launch {
				db.citizenDao().insertCitizen(citizen)
				loadCitizenList()
			}
		}
		
		resetButton.setOnClickListener {
			lifecycleScope.launch {
				db.citizenDao().deleteAllCitizens()
				loadCitizenList()
			}
		}
		
		loadCitizenList()
	}
	
	private fun loadCitizenList() {
		lifecycleScope.launch {
			val list = db.citizenDao().getAllCitizens()
			runOnUiThread {
				citizenAdapter.updateData(list)
			}
		}
	}
}
