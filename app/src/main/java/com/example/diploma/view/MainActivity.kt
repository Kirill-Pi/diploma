package com.example.diploma.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.diploma.data.Events
import com.example.diploma.data.Favorites
import com.example.diploma.data.Launch
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.viewmodel.EventsViewModel
import com.example.pigolevmyapplication.R
import com.example.pigolevmyapplication.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        menuInit()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, EventsFragment())
            .addToBackStack("home")
            .commit()
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount == 1) {
                    showAppClosingDialog()
                }
                else supportFragmentManager.popBackStack()
            }
        }


    private fun menuInit() {
        var bottomNavigation = binding.bottomNavigation
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.events -> {
                    val tag = "events"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: EventsFragment(), tag)
                    true
                }
                R.id.launches -> {
                    val tag = "launches"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: Launches(), tag)
                    true
                }
                R.id.spacecrafts -> {
                    val tag = "spacecrafts"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: SpaceShips(), tag)
                    true
                }
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: FavoritesFragment(), tag)
                    true
                }
                R.id.settings -> {
                    val tag = "settings"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: Settings(), tag)
                    true
                }
                else -> false
            }
        }
    }

    private fun showAppClosingDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Вы хотите выйти?")
            // .setMessage("Серьезно??")
            .setPositiveButton("Да") { _, _ -> finish() }
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun checkFragmentExistence(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, tag)
            // .addToBackStack(null)
            .commit()
    }

    fun launchDetailsSCFragment(spaceCraft: SpacecraftConfig) {
        val bundle = Bundle()
        bundle.putParcelable("spaceCraft", spaceCraft)
        val fragment = DetailsSC()
        fragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack("spacecraft")
            .commit()
    }

    fun launchDetailsLaunchFragment (launch: Launch) {
        val bundle = Bundle()
        bundle.putParcelable("launch", launch)
        val fragment = DetailsLaunch()
        fragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack("launch")
            .commit()
    }

    fun launchDetailsEventFragment (event: Events) {
        val bundle = Bundle()
        bundle.putParcelable("event", event)
        val fragment = DetailEvents()
        fragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack("event")
            .commit()
    }

    fun launchDetailsFavoritesFragment (spaceCraft: Favorites) {
        println(spaceCraft)
        val bundle = Bundle()
        bundle.putParcelable("favorites", spaceCraft)
        val fragment = DetailFavorites()
        fragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack("favorites")
            .commit()
    }
}