package com.example.playlistmaker.ui.main.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.utils.Tools

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_navbar)
        if (Tools.isBackgroundColorLight(ContextCompat.getColor(this, R.color.background_navbar))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            else window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newPlaylistFragment -> hideBottomNavigation()
                R.id.playerFragment -> hideBottomNavigation()
                R.id.playlistsFragment -> hideBottomNavigation()
                R.id.singlePlaylist -> hideBottomNavigation()
                R.id.editPlaylistFragment -> hideBottomNavigation()
                else -> showBottomNavigation()
            }
        }
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigationView.isVisible = false
    }

    private fun showBottomNavigation() {
        binding.bottomNavigationView.isVisible = true
    }
}
