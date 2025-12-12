package com.example.tabgames

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kotlin.math.sqrt

class MTActivity : AppCompatActivity() {

    // دکمه‌ها و لیبل‌ها
    private lateinit var tiles: Array<MaterialButton>
    private lateinit var labels: Array<View>

    // برای ریست (موقعیت اولیه)
    private lateinit var startTileX: FloatArray
    private lateinit var startTileY: FloatArray
    private lateinit var startLabelX: FloatArray
    private lateinit var startLabelY: FloatArray

    private var positionsCaptured = false

    // برای drag
    private var dx = 0f
    private var dy = 0f

    // فاصله برای match
    private val snap = 120f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mt_page)

        val contentRoot = findViewById<View>(android.R.id.content)

        tiles = arrayOf(
            findViewById(R.id.tile1), findViewById(R.id.tile2), findViewById(R.id.tile3), findViewById(R.id.tile4),
            findViewById(R.id.tile5), findViewById(R.id.tile6), findViewById(R.id.tile7), findViewById(R.id.tile8),
            findViewById(R.id.tile9), findViewById(R.id.tile10), findViewById(R.id.tile11), findViewById(R.id.tile12),
            findViewById(R.id.tile13), findViewById(R.id.tile14)
        )

        labels = arrayOf(
            findViewById(R.id.label1), findViewById(R.id.label2), findViewById(R.id.label3), findViewById(R.id.label4),
            findViewById(R.id.label5), findViewById(R.id.label6), findViewById(R.id.label7), findViewById(R.id.label8),
            findViewById(R.id.label9), findViewById(R.id.label10), findViewById(R.id.label11), findViewById(R.id.label12),
            findViewById(R.id.label13), findViewById(R.id.label14)
        )

        // ذخیره جای اولیه برای ریست
        startTileX = FloatArray(tiles.size)
        startTileY = FloatArray(tiles.size)
        startLabelX = FloatArray(labels.size)
        startLabelY = FloatArray(labels.size)

        contentRoot.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    captureStartPositions()
                    if (positionsCaptured) {
                        contentRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            })

        // فعال کردن drag برای همه
        for (i in tiles.indices) {
            makeDraggable(i)
        }

        // ریست
        findViewById<View>(R.id.btnReset).setOnClickListener {
            resetAll()
        }
    }

    private fun resetAll() {
        captureStartPositions()
        for (i in tiles.indices) {
            tiles[i].x = startTileX[i]
            tiles[i].y = startTileY[i]
            tiles[i].translationX = 0f
            tiles[i].translationY = 0f
            tiles[i].visibility = View.VISIBLE

            labels[i].x = startLabelX[i]
            labels[i].y = startLabelY[i]
            labels[i].translationX = 0f
            labels[i].translationY = 0f
            labels[i].visibility = View.VISIBLE
        }
    }

    private fun captureStartPositions() {
        if (positionsCaptured) return
        for (i in tiles.indices) {
            startTileX[i] = tiles[i].x
            startTileY[i] = tiles[i].y
            startLabelX[i] = labels[i].x
            startLabelY[i] = labels[i].y
        }
        positionsCaptured = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun makeDraggable(index: Int) {
        val tile = tiles[index]
        val label = labels[index]

        tile.setOnTouchListener { v, e ->
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dx = e.rawX - v.x
                    dy = e.rawY - v.y
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = e.rawX - dx
                    val newY = e.rawY - dy

                    v.x = newX
                    v.y = newY

                    // لیبل هم همراهش بیاد
                    label.x = newX
                    label.y = newY
                    true
                }

                MotionEvent.ACTION_UP -> {
                    checkMatch(index)
                    true
                }

                else -> false
            }
        }
    }

    private fun checkMatch(movedIndex: Int) {
        val movedTile = tiles[movedIndex]
        if (movedTile.visibility != View.VISIBLE) return

        val t = movedTile.text.toString()

        for (i in tiles.indices) {
            if (i == movedIndex) continue
            if (tiles[i].visibility != View.VISIBLE) continue
            if (tiles[i].text.toString() != t) continue

            val dist = distance(movedTile, tiles[i])
            if (dist < snap) {
                // حذف هر دو
                tiles[movedIndex].visibility = View.GONE
                labels[movedIndex].visibility = View.GONE

                tiles[i].visibility = View.GONE
                labels[i].visibility = View.GONE
                break
            }
        }
    }

    private fun distance(a: View, b: View): Float {
        val ax = a.x
        val ay = a.y
        val bx = b.x
        val by = b.y

        val dx = ax - bx
        val dy = ay - by
        return sqrt(dx * dx + dy * dy)
    }
}
