package com.example.proiecthelpwithhomework

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.feature.auth.AuthActivity
import com.example.feature.auth.data.session.SessionManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        sessionManager = SessionManager(this)
        
        val userDisplay = findViewById<TextView>(R.id.userDisplay)
        lifecycleScope.launch {
            sessionManager.userNameFlow.collect { name ->
                userDisplay.text = name ?: "Anonim"
            }
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = MainPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Feed"
                1 -> "Adaugă"
                else -> "Activitate"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, AccountSettingsActivity::class.java))
                true
            }
            R.id.action_logout -> {
                lifecycleScope.launch {
                    sessionManager.setLoggedIn(false)
                    startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}