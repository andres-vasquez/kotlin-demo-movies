package com.udacity.nano.popularmovies

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.databinding.ActivityMainBinding
import com.udacity.nano.popularmovies.databinding.NavHeaderBinding
import com.udacity.nano.popularmovies.utils.sendNotification
import com.udacity.nano.popularmovies.utils.setLocale
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private var navViewHeaderBinding: NavHeaderBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository: MovieRepositoryI by inject()
        repository.getUserPrefs()?.let { setLocale(this, it.language) }

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        navController = this.findNavController(R.id.nav_host_fragment)
        // prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
            when (nd.id) {
                R.id.splashFragment -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    updateUserInfo(binding)
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        binding.navView.setNavigationItemSelectedListener(this)

        updateUserInfo(binding)
        binding.lifecycleOwner = this
    }

    private fun updateUserInfo(binding: ActivityMainBinding) {
        val repository: MovieRepositoryI by inject()
        repository.getUserPrefs()?.let {
            val viewHeader = binding.navView.getHeaderView(0)
            if (navViewHeaderBinding == null) {
                navViewHeaderBinding = NavHeaderBinding.bind(viewHeader)
            }
            navViewHeaderBinding!!.user = it
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        //return navController.navigateUp()
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onNavigationItemSelected(@NonNull menuItem: MenuItem): Boolean {
        drawerLayout.closeDrawers()
        val id: Int = menuItem.getItemId()
        when (id) {
            R.id.logout -> navController.navigate(R.id.loginFragment)
            R.id.about -> sendNotification(this)
        }
        return true
    }
}