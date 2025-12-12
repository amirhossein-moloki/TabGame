package com.example.tabgames

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class DozDozActivity : AppCompatActivity() {

    private var xTurn = true

    private lateinit var b00: MaterialButton
    private lateinit var b01: MaterialButton
    private lateinit var b02: MaterialButton
    private lateinit var b10: MaterialButton
    private lateinit var b11: MaterialButton
    private lateinit var b12: MaterialButton
    private lateinit var b20: MaterialButton
    private lateinit var b21: MaterialButton
    private lateinit var b22: MaterialButton

    private lateinit var all: List<MaterialButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dozdoz_page)

        val btnStart = findViewById<MaterialButton>(R.id.btnStart)
        val btnReset = findViewById<MaterialButton>(R.id.btnReset)

        b00 = findViewById(R.id.btn00)
        b01 = findViewById(R.id.btn01)
        b02 = findViewById(R.id.btn02)
        b10 = findViewById(R.id.btn10)
        b11 = findViewById(R.id.btn11)
        b12 = findViewById(R.id.btn12)
        b20 = findViewById(R.id.btn20)
        b21 = findViewById(R.id.btn21)
        b22 = findViewById(R.id.btn22)

        all = listOf(b00, b01, b02, b10, b11, b12, b20, b21, b22)

        btnStart.setOnClickListener {
            resetGame()
            Toast.makeText(this, "شروع شد", Toast.LENGTH_SHORT).show()
        }

        btnReset.setOnClickListener {
            resetGame()
            Toast.makeText(this, "ریست شد", Toast.LENGTH_SHORT).show()
        }

        b00.setOnClickListener { play(b00) }
        b01.setOnClickListener { play(b01) }
        b02.setOnClickListener { play(b02) }
        b10.setOnClickListener { play(b10) }
        b11.setOnClickListener { play(b11) }
        b12.setOnClickListener { play(b12) }
        b20.setOnClickListener { play(b20) }
        b21.setOnClickListener { play(b21) }
        b22.setOnClickListener { play(b22) }

        resetGame()
    }

    private fun resetGame() {
        for (b in all) {
            b.text = ""
            b.isEnabled = true
            b.setTextColor(Color.BLACK)
        }
        xTurn = true
    }

    private fun play(btn: MaterialButton) {
        val p = if (xTurn) "X" else "O"
        btn.text = p
        btn.isEnabled = false
        btn.setTextColor(if (p == "X") Color.RED else Color.BLUE)

        if (checkWin(p)) {
            for (b in all) b.isEnabled = false
            Toast.makeText(this, "$p برنده شد!", Toast.LENGTH_SHORT).show()
            return
        }

        val anyEmpty = all.any { it.isEnabled }
        if (!anyEmpty) {
            Toast.makeText(this, "مساوی!", Toast.LENGTH_SHORT).show()
            return
        }

        xTurn = !xTurn
    }

    private fun checkWin(p: String): Boolean {
        // ردیف‌ها
        if (b00.text == p && b01.text == p && b02.text == p) return true
        if (b10.text == p && b11.text == p && b12.text == p) return true
        if (b20.text == p && b21.text == p && b22.text == p) return true

        // ستون‌ها
        if (b00.text == p && b10.text == p && b20.text == p) return true
        if (b01.text == p && b11.text == p && b21.text == p) return true
        if (b02.text == p && b12.text == p && b22.text == p) return true

        // قطرها
        if (b00.text == p && b11.text == p && b22.text == p) return true
        if (b02.text == p && b11.text == p && b20.text == p) return true

        return false
    }
}
