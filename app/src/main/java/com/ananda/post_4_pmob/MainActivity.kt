package com.ananda.post_4_pmob

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
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
			val fullName = fullNameInput.text.toString().trim()
			val nikText = nationalIdInput.text.toString().trim()
			val province = provinceInput.text.toString().trim()
			val district = districtInput.text.toString().trim()
			val village = villageInput.text.toString().trim()
			val rtText = rtInput.text.toString()
			val rwText = rwInput.text.toString()
			val genderId = genderGroup.checkedRadioButtonId
			val maritalStatus = maritalStatusSpinner.selectedItem.toString()
			
			if (fullName.isEmpty()) {
				Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			if (nikText.isEmpty()) {
				Toast.makeText(this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			if (nikText.length != 16 || nikText.toLongOrNull() == null) {
				Toast.makeText(this, "NIK harus berupa 16 digit angka!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			if (province.isEmpty() || district.isEmpty() || village.isEmpty()) {
				Toast.makeText(this, "Alamat tidak boleh kosong!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			if (rtText.isEmpty() || rwText.isEmpty()) {
				Toast.makeText(this, "RT dan RW tidak boleh kosong!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			val rtValue = rtText.toIntOrNull()
			val rwValue = rwText.toIntOrNull()
			
			if (rtValue == null || rwValue == null) {
				Toast.makeText(this, "RT dan RW harus berupa angka!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			if (genderId == -1) {
				Toast.makeText(this, "Harap pilih jenis kelamin!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			if (maritalStatus.equals("Pilih Status", ignoreCase = true)) {
				Toast.makeText(this, "Harap pilih status pernikahan!", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			
			val gender = when (genderId) {
				R.id.rbLaki -> "Laki-Laki"
				R.id.rbPerempuan -> "Perempuan"
				else -> "-"
			}
			
			val citizen = CitizenModel(
				fullName = fullName,
				nationalId = nikText,
				province = province,
				district = district,
				village = village,
				rt = rtValue,
				rw = rwValue,
				gender = gender,
				maritalStatus = maritalStatus
			)
			
			lifecycleScope.launch {
				db.citizenDao().insertCitizen(citizen)
				loadCitizenList()
			}
			
			Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
			
			fullNameInput.text.clear()
			nationalIdInput.text.clear()
			provinceInput.text.clear()
			districtInput.text.clear()
			villageInput.text.clear()
			rtInput.text.clear()
			rwInput.text.clear()
			genderGroup.clearCheck()
			maritalStatusSpinner.setSelection(0)
		}
		
		resetButton.setOnClickListener {
			lifecycleScope.launch {
				db.citizenDao().deleteAllCitizens()
				loadCitizenList()
			}
			Toast.makeText(this, "Semua data dihapus!", Toast.LENGTH_SHORT).show()
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
