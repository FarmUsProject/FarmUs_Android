package com.example.farmus_application.ui.home.Adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farmus_application.R
import com.example.farmus_application.databinding.RvLocalFarmBinding
import com.example.farmus_application.ui.home.RVFarmDataModel
import java.util.Date.from


class LocalFarmRVAdapter(val items: MutableList<RVFarmDataModel>): RecyclerView.Adapter<LocalFarmRVAdapter.ViewHolder>() {

    private lateinit var localFarmBinding : RvLocalFarmBinding

    override fun getItemCount(): Int {

        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalFarmRVAdapter.ViewHolder{

        localFarmBinding = RvLocalFarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //테두리 둥글게 설정
        localFarmBinding.rvFarmImage.clipToOutline = true

        return ViewHolder(localFarmBinding)
    }

    override fun onBindViewHolder(holder: LocalFarmRVAdapter.ViewHolder, position: Int) {
         holder.bindItems(items[position])
    }

    inner class ViewHolder(binding: RvLocalFarmBinding): RecyclerView.ViewHolder(binding.root){

        fun bindItems(item : RVFarmDataModel){

            //이미지 어떻게 넣을지 수정!!
            localFarmBinding.rvFarmImage.setImageResource(item.farm_image)
            localFarmBinding.rvFarmName.text = item.farm_name
            localFarmBinding.rvFarmSize.text = item.farm_size
            localFarmBinding.rvFarmPrice.text = item.farm_price
        }
    }
}