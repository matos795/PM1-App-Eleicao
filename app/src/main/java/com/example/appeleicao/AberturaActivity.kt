package com.example.appeleicao

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Tela introdutória do trabalho: exibe a imagem e depois abre o login.
class AberturaActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var abrirEm = 0L
    private var navegou = false

    private val abrirLogin = Runnable {
        if (!navegou && !isFinishing && !isDestroyed) {
            navegou = true
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_abertura)

        abrirEm = savedInstanceState?.getLong("abrirEm")
            ?: (SystemClock.uptimeMillis() + 2500L)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(abrirLogin, (abrirEm - SystemClock.uptimeMillis()).coerceAtLeast(0L))
    }

    override fun onPause() {
        handler.removeCallbacks(abrirLogin)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("abrirEm", abrirEm)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        handler.removeCallbacks(abrirLogin)
        super.onDestroy()
    }
}
