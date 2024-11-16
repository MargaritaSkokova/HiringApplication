package com.maran.test

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.maran.test.database.VacancyDatabase
import com.maran.test.database.VacancyRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val statusBarHeight = resources.getDimensionPixelSize(
            resources.getIdentifier("status_bar_height", "dimen", "android")
        )

        VacancyRepository.initialize(this)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val nav = this.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        nav.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        nav.setupWithNavController(navController)
        nav.itemActiveIndicatorColor = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_pressed)
            ), intArrayOf(
                getColor(R.color.blue),
                getColor(R.color.blue),
                getColor(R.color.blue),
                getColor(R.color.blue)
            )
        )

        nav.selectedItemId = R.id.signInFragment

        findViewById<View>(R.id.nav_host_fragment).setPadding(0, statusBarHeight, 0, 0)
        enableEdgeToEdge()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController)
                || super.onOptionsItemSelected(item)
    }
}

