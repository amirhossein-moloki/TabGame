package com.example.tabgames

import android.os.Bundle
import android.widget.TabHost
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val tabHost = findViewById<TabHost>(R.id.tabHost)
        tabHost.setup()

        val dozView  = layoutInflater.inflate(R.layout.dozdoz_page, null)
        val mtView   = layoutInflater.inflate(R.layout.mt_page, null)
        val mainView = layoutInflater.inflate(R.layout.main_page, null)

        val dozTab = tabHost.newTabSpec("doz")
        dozTab.setIndicator("دوز دوز")
        dozTab.setContent {dozView}
        tabHost.addTab(dozTab)

        val mtTab = tabHost.newTabSpec("mt")
        mtTab.setIndicator("ماجونگ")
        mtTab.setContent {mtView}
        tabHost.addTab(mtTab)

        val mainTab = tabHost.newTabSpec("main")
        mainTab.setIndicator("تب اصلی")
        mainTab.setContent {mainView}
        tabHost.addTab(mainTab)



    }
}