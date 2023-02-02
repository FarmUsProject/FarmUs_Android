package com.example.farmus_application.ui.farm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmus_application.databinding.ActivityFarmDetailBinding

class FarmDetailActivity : AppCompatActivity() {

    private lateinit var farmDetailBinding: ActivityFarmDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        farmDetailBinding = ActivityFarmDetailBinding.inflate(layoutInflater)
        setContentView(farmDetailBinding.root)


    }
}