package com.example.farmus_application.ui.farm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.farmus_application.databinding.RvMyFarmItemBinding

class FarmRVAdapter(val items : MutableList<farmDataModel>) : RecyclerView.Adapter<FarmRVAdapter.ViewHolder>() {

    private lateinit var rvBinding: RvMyFarmItemBinding

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : FarmRVAdapter.ViewHolder {
        rvBinding = RvMyFarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        rvBinding.rvItemImg.clipToOutline = true
        return ViewHolder(rvBinding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ViewHolder( binding: RvMyFarmItemBinding) :RecyclerView.ViewHolder(binding.root) {

        fun bind(item : farmDataModel) {
            rvBinding.rvItemImg.setImageResource(item.image)
            rvBinding.rvItemTitle.text = item.title
            rvBinding.rvItemStartDay.text = item.startDay
            rvBinding.rvItemEndDay.text = item.endDay
        }
    }

}