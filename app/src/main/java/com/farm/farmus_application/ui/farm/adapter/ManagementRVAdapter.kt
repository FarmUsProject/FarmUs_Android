package com.farm.farmus_application.ui.farm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farm.farmus_application.R
import com.farm.farmus_application.databinding.ItemManagementCalnedarBinding
import com.farm.farmus_application.model.farm.unavailableDate.UnavailableDateInfo

interface OnDeleteClickListener {
    fun onDeleteClick(farmDateID: Int)
}

class ManagementRVAdapter(
    private val onDeleteClickListener: OnDeleteClickListener
) :
    ListAdapter<UnavailableDateInfo, ManagementRVAdapter.ManagementViewHolder>(diffUtil) {

    private lateinit var binding: ItemManagementCalnedarBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagementViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_management_calnedar,
            parent,
            false
        )
        return ManagementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManagementViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ManagementViewHolder(private val binding: ItemManagementCalnedarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UnavailableDateInfo) {
            val formattedStartDate = item.getFormattedStartDate()
            val formattedEndDate = item.getFormattedEndDate()

            binding.unAvailableDate = item
            binding.unavailableDateTextView.text = "$formattedStartDate~$formattedEndDate"


            binding.unavailableClearButton.setOnClickListener {
                onDeleteClickListener.onDeleteClick(item.FarmDateID)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UnavailableDateInfo>() {
            override fun areItemsTheSame(
                oldItem: UnavailableDateInfo,
                newItem: UnavailableDateInfo
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: UnavailableDateInfo,
                newItem: UnavailableDateInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}