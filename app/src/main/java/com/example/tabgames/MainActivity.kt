package com.example.tabgames

import android.app.LocalActivityManager
import android.content.Intent
import android.os.Bundle
import android.widget.TabHost
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var activityManager: LocalActivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        activityManager = LocalActivityManager(this, true)
        activityManager.dispatchCreate(savedInstanceState)

        val tabHost = findViewById<TabHost>(R.id.tabHost)
        tabHost.setup(activityManager)

        val dozTab = tabHost.newTabSpec("doz")
        dozTab.setIndicator("دوز دوز")
        dozTab.setContent(Intent(this, DozDozActivity::class.java))
        tabHost.addTab(dozTab)

        val mtTab = tabHost.newTabSpec("mt")
        mtTab.setIndicator("ماجونگ")
        mtTab.setContent(Intent(this, MTActivity::class.java))
        tabHost.addTab(mtTab)

        val mainView = layoutInflater.inflate(R.layout.main_page, tabHost.tabContentView, false)
        val mainTab = tabHost.newTabSpec("main")
        mainTab.setIndicator("تب اصلی")
        mainTab.setContent { _ -> mainView }
        tabHost.addTab(mainTab)

        mainView.findViewById<android.widget.Button>(R.id.btnStart).setOnClickListener {
            tabHost.currentTab = 0
        }

        tabHost.currentTab = 2
    }

    override fun onResume() {
        super.onResume()
        activityManager.dispatchResume()
    }

    override fun onPause() {
        super.onPause()
        activityManager.dispatchPause(isFinishing)
    }
}
