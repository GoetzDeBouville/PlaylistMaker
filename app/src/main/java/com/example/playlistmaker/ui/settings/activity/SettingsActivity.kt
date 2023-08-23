package com.example.playlistmaker.ui.settings.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModelFactory
import org.koin.android.ext.android.inject

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

//    private lateinit var viewModel: SettingsViewModel
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = SettingsViewModelFactory(
            Creator.provideSharingInteractor(application),
            Creator.provideSettingsInteractor(application)
        )

//        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        viewModel.themeSettings.observe(this) { isDarkTheme ->
            binding.themeSwitcher.setChecked(isDarkTheme)
        }

        binding.llArrowBack.setOnClickListener { finish() }

        binding.shareApp.setOnClickListener {
            viewModel.shareApp()
        }

        binding.textToSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.openTerms()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { isChecked ->
            viewModel.updateThemeSettings(isChecked)
        }
    }
}
