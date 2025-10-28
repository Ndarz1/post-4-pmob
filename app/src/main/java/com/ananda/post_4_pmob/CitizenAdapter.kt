package com.ananda.post_4_pmob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CitizenAdapter(
	private var citizenList: List<CitizenModel>
) : RecyclerView.Adapter<CitizenAdapter.CitizenViewHolder>() {
	
	class CitizenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val tvName: TextView = view.findViewById(R.id.tvName)
		val tvId: TextView = view.findViewById(R.id.tvId)
		val tvAddress: TextView = view.findViewById(R.id.tvAddress)
		val tvDetail: TextView = view.findViewById(R.id.tvDetail)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitizenViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.item_citizen, parent, false)
		return CitizenViewHolder(view)
	}
	
	override fun onBindViewHolder(holder: CitizenViewHolder, position: Int) {
		val citizen = citizenList[position]
		holder.tvName.text = citizen.fullName
		holder.tvId.text = "NIK: ${citizen.nationalId}"
		holder.tvAddress.text =
			"${citizen.village}, ${citizen.district}, ${citizen.province} (RT ${citizen.rt}/RW ${citizen.rw})"
		holder.tvDetail.text = "${citizen.gender} - ${citizen.maritalStatus}"
	}
	
	override fun getItemCount(): Int = citizenList.size
	
	fun updateData(newList: List<CitizenModel>) {
		citizenList = newList
		notifyDataSetChanged()
	}
}