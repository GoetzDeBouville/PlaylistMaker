package com.example.playlistmaker.ui.main.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.main.BottomNavigationController

class MainActivity : AppCompatActivity(), BottomNavigationController {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun hideBottomNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    override fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}
