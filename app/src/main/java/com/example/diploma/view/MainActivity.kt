package com.example.diploma.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.diploma.view.Events
import com.example.diploma.view.Launches
import com.example.diploma.view.Settings
import com.example.diploma.view.SpaceShips
import com.example.diploma.viewmodel.SpaceShipsViewModel
import com.example.pigolevmyapplication.R
import com.example.pigolevmyapplication.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {


    var spaceShipViewmodel = SpaceShipsViewModel()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)
        menuInit()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, Events())
            .addToBackStack("home")
            .commit()

        spaceShipViewmodel.interactorStart()
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
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)

                    changeFragment( fragment?: Events(), tag)
                    true
                }
                R.id.launches -> {
                    val tag = "favorites"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: Launches(), tag)

                    true
                }
                R.id.spacecrafts -> {
                    val tag = "watch_later"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: SpaceShips(), tag)
                    true
                }
                R.id.settings -> {
                    val tag = "selections"
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
}