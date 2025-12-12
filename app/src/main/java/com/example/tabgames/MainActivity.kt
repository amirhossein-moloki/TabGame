package com.example.tabgames

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TabHost
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    // --- Variables for DozDoz Game ---
    private var dozXTurn = true
    private lateinit var dozB00: MaterialButton
    private lateinit var dozB01: MaterialButton
    private lateinit var dozB02: MaterialButton
    private lateinit var dozB10: MaterialButton
    private lateinit var dozB11: MaterialButton
    private lateinit var dozB12: MaterialButton
    private lateinit var dozB20: MaterialButton
    private lateinit var dozB21: MaterialButton
    private lateinit var dozB22: MaterialButton
    private lateinit var dozAllButtons: List<MaterialButton>

    // --- Variables for MT Game ---
    private lateinit var mtTiles: Array<MaterialButton>
    private lateinit var mtLabels: Array<View>
    private lateinit var mtStartTileX: FloatArray
    private lateinit var mtStartTileY: FloatArray
    private lateinit var mtStartLabelX: FloatArray
    private lateinit var mtStartLabelY: FloatArray
    private var mtDx = 0f
    private var mtDy = 0f
    private val mtSnap = 120f


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

        // Inflate views for each tab
        val dozView = layoutInflater.inflate(R.layout.dozdoz_page, null)
        val mtView = layoutInflater.inflate(R.layout.mt_page, null)
        val mainView = layoutInflater.inflate(R.layout.main_page, null)

        // Setup the logic for each game view
        setupDozDozTab(dozView)
        setupMTTab(mtView)

        // Create and add tabs
        val dozTab = tabHost.newTabSpec("doz")
        dozTab.setIndicator("دوز دوز")
        dozTab.setContent { dozView }
        tabHost.addTab(dozTab)

        val mtTab = tabHost.newTabSpec("mt")
        mtTab.setIndicator("ماجونگ")
        mtTab.setContent { mtView }
        tabHost.addTab(mtTab)

        val mainTab = tabHost.newTabSpec("main")
        mainTab.setIndicator("تب اصلی")
        mainTab.setContent { mainView }
        tabHost.addTab(mainTab)

        tabHost.currentTab = 2 // Set main tab as default
    }

    // --- Methods for DozDoz Game ---

    private fun setupDozDozTab(view: View) {
        val btnStart = view.findViewById<MaterialButton>(R.id.btnStart)
        val btnReset = view.findViewById<MaterialButton>(R.id.btnReset)

        dozB00 = view.findViewById(R.id.btn00)
        dozB01 = view.findViewById(R.id.btn01)
        dozB02 = view.findViewById(R.id.btn02)
        dozB10 = view.findViewById(R.id.btn10)
        dozB11 = view.findViewById(R.id.btn11)
        dozB12 = view.findViewById(R.id.btn12)
        dozB20 = view.findViewById(R.id.btn20)
        dozB21 = view.findViewById(R.id.btn21)
        dozB22 = view.findViewById(R.id.btn22)

        dozAllButtons = listOf(dozB00, dozB01, dozB02, dozB10, dozB11, dozB12, dozB20, dozB21, dozB22)

        btnStart.setOnClickListener {
            resetDozGame()
            Toast.makeText(this, "شروع شد", Toast.LENGTH_SHORT).show()
        }

        btnReset.setOnClickListener {
            resetDozGame()
            Toast.makeText(this, "ریست شد", Toast.LENGTH_SHORT).show()
        }

        dozB00.setOnClickListener { playDoz(dozB00) }
        dozB01.setOnClickListener { playDoz(dozB01) }
        dozB02.setOnClickListener { playDoz(dozB02) }
        dozB10.setOnClickListener { playDoz(dozB10) }
        dozB11.setOnClickListener { playDoz(dozB11) }
        dozB12.setOnClickListener { playDoz(dozB12) }
        dozB20.setOnClickListener { playDoz(dozB20) }
        dozB21.setOnClickListener { playDoz(dozB21) }
        dozB22.setOnClickListener { playDoz(dozB22) }

        resetDozGame()
    }

    private fun resetDozGame() {
        for (b in dozAllButtons) {
            b.text = ""
            b.isEnabled = true
            b.setTextColor(Color.BLACK)
        }
        dozXTurn = true
    }

    private fun playDoz(btn: MaterialButton) {
        val p = if (dozXTurn) "X" else "O"
        btn.text = p
        btn.isEnabled = false
        btn.setTextColor(if (p == "X") Color.RED else Color.BLUE)

        if (checkDozWin(p)) {
            for (b in dozAllButtons) b.isEnabled = false
            Toast.makeText(this, "$p برنده شد!", Toast.LENGTH_SHORT).show()
            return
        }

        val anyEmpty = dozAllButtons.any { it.isEnabled }
        if (!anyEmpty) {
            Toast.makeText(this, "مساوی!", Toast.LENGTH_SHORT).show()
            return
        }

        dozXTurn = !dozXTurn
    }

    private fun checkDozWin(p: String): Boolean {
        if (dozB00.text == p && dozB01.text == p && dozB02.text == p) return true
        if (dozB10.text == p && dozB11.text == p && dozB12.text == p) return true
        if (dozB20.text == p && dozB21.text == p && dozB22.text == p) return true
        if (dozB00.text == p && dozB10.text == p && dozB20.text == p) return true
        if (dozB01.text == p && dozB11.text == p && dozB21.text == p) return true
        if (dozB02.text == p && dozB12.text == p && dozB22.text == p) return true
        if (dozB00.text == p && dozB11.text == p && dozB22.text == p) return true
        if (dozB02.text == p && dozB11.text == p && dozB20.text == p) return true
        return false
    }

    // --- Methods for MT Game ---

    private fun setupMTTab(view: View) {
        mtTiles = arrayOf(
            view.findViewById(R.id.tile1), view.findViewById(R.id.tile2), view.findViewById(R.id.tile3), view.findViewById(R.id.tile4),
            view.findViewById(R.id.tile5), view.findViewById(R.id.tile6), view.findViewById(R.id.tile7), view.findViewById(R.id.tile8),
            view.findViewById(R.id.tile9), view.findViewById(R.id.tile10), view.findViewById(R.id.tile11), view.findViewById(R.id.tile12),
            view.findViewById(R.id.tile13), view.findViewById(R.id.tile14)
        )

        mtLabels = arrayOf(
            view.findViewById(R.id.label1), view.findViewById(R.id.label2), view.findViewById(R.id.label3), view.findViewById(R.id.label4),
            view.findViewById(R.id.label5), view.findViewById(R.id.label6), view.findViewById(R.id.label7), view.findViewById(R.id.label8),
            view.findViewById(R.id.label9), view.findViewById(R.id.label10), view.findViewById(R.id.label11), view.findViewById(R.id.label12),
            view.findViewById(R.id.label13), view.findViewById(R.id.label14)
        )

        mtStartTileX = FloatArray(mtTiles.size)
        mtStartTileY = FloatArray(mtTiles.size)
        mtStartLabelX = FloatArray(mtLabels.size)
        mtStartLabelY = FloatArray(mtLabels.size)

        view.post { // Wait for layout to be measured to get correct positions
            for (i in mtTiles.indices) {
                mtStartTileX[i] = mtTiles[i].translationX
                mtStartTileY[i] = mtTiles[i].translationY
                mtStartLabelX[i] = mtLabels[i].translationX
                mtStartLabelY[i] = mtLabels[i].translationY
            }
        }


        for (i in mtTiles.indices) {
            makeMTDraggable(i)
        }

        view.findViewById<View>(R.id.btnReset).setOnClickListener {
            resetMTAll()
        }
    }

    private fun resetMTAll() {
        for (i in mtTiles.indices) {
            mtTiles[i].translationX = mtStartTileX[i]
            mtTiles[i].translationY = mtStartTileY[i]
            mtTiles[i].visibility = View.VISIBLE

            mtLabels[i].translationX = mtStartLabelX[i]
            mtLabels[i].translationY = mtStartLabelY[i]
            mtLabels[i].visibility = View.VISIBLE
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun makeMTDraggable(index: Int) {
        val tile = mtTiles[index]
        val label = mtLabels[index]

        tile.setOnTouchListener { v, e ->
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    mtDx = v.translationX - e.rawX
                    mtDy = v.translationY - e.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = e.rawX + mtDx
                    val newY = e.rawY + mtDy
                    v.translationX = newX
                    v.translationY = newY
                    label.translationX = newX
                    label.translationY = newY
                    true
                }

                MotionEvent.ACTION_UP -> {
                    checkMTMatch(index)
                    true
                }

                else -> false
            }
        }
    }

    private fun checkMTMatch(movedIndex: Int) {
        val movedTile = mtTiles[movedIndex]
        if (movedTile.visibility != View.VISIBLE) return

        val t = movedTile.text.toString()

        for (i in mtTiles.indices) {
            if (i == movedIndex) continue
            if (mtTiles[i].visibility != View.VISIBLE) continue
            if (mtTiles[i].text.toString() != t) continue

            val dist = distanceMT(movedTile, mtTiles[i])
            if (dist < mtSnap) {
                mtTiles[movedIndex].visibility = View.GONE
                mtLabels[movedIndex].visibility = View.GONE

                mtTiles[i].visibility = View.GONE
                mtLabels[i].visibility = View.GONE
                break
            }
        }
    }

    private fun distanceMT(a: View, b: View): Float {
        val ax = a.x + a.translationX
        val ay = a.y + a.translationY
        val bx = b.x + b.translationX
        val by = b.y + b.translationY

        val dx = ax - bx
        val dy = ay - by
        return sqrt(dx * dx + dy * dy)
    }
}
