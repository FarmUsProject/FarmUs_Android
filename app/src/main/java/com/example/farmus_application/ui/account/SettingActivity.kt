package com.example.farmus_application.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmus_application.databinding.ActivityMainBinding
import com.example.farmus_application.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var settingBinding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(settingBinding.root)
    }
}