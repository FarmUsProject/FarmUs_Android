package com.example.farmus_application.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmus_application.databinding.ActivityProfileSettingBinding
import com.example.farmus_application.databinding.ActivitySettingBinding

class ProfileSettingActivity : AppCompatActivity() {

    private lateinit var profilesettingBinding: ActivityProfileSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(profilesettingBinding.root)
    }
}