package com.example.smartinteract

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

class Multimedia:AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.multimedia)

        val botonSonido = findViewById<ImageButton>(R.id.reproducir)
        val botonpausar = findViewById<ImageButton>(R.id.parar)
        val botonReiniciar = findViewById<ImageButton>(R.id.reiniciar)
        val home= findViewById<ImageButton>(R.id.menu_home)
        val sensores = findViewById<ImageButton>(R.id.menu_sensores)
        val conexionAPI = findViewById<ImageButton>(R.id.menu_API)
        val galeria = findViewById<ImageButton>(R.id.menu_imagenes)
        val multimedia = findViewById<ImageButton>(R.id.menu_multimedia)
        val menu = findViewById<ImageButton>(R.id.buttonPopupMenu)

        gestureDetector = GestureDetectorCompat(this, GestureListener(this))

        menu.setOnClickListener {
            showPopupMenu(it)
        }
        home.setOnClickListener {
            val intent = Intent(this, App::class.java)
            startActivity(intent)
        }
        sensores.setOnClickListener {
            val intent = Intent(this, Sensores::class.java)
            startActivity(intent)
        }
        conexionAPI.setOnClickListener {
            val intent = Intent(this, ConexionAPI::class.java)
            startActivity(intent)
        }
        galeria.setOnClickListener {
            val intent = Intent(this, Galeria::class.java)
            startActivity(intent)
        }
        multimedia.setOnClickListener {
            val intent = Intent(this, Multimedia::class.java)
            startActivity(intent)
        }


        val mediaPlayer = MediaPlayer.create(this, R.raw.gigachad)
        botonSonido.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.seekTo(0) // Reiniciar al principio
            } else {
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            }
        }
        botonpausar.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
               mediaPlayer.start()
            }else {
                mediaPlayer.pause()
            }
        }

        botonReiniciar.setOnClickListener {
            mediaPlayer.seekTo(0)

        }
    }


    inner class GestureListener(val context: Multimedia) : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffX = e2.x - (e1?.x ?: 0f)
            val diffY = e2.y - (e1?.y ?: 0f)

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (diffX > 0) {
                    val intent = Intent(context, App::class.java)
                    context.startActivity(intent)
                } else {
                    showToast("Deslizado a la izquierda")
                }
            }
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            showToast("Toque detectado")
            return super.onSingleTapUp(e)
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Utilizamos gestureDetector para manejar el evento
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menutopright, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> startActivity(Intent(this, App::class.java))
                R.id.sensores -> startActivity(Intent(this, Sensores::class.java))
                R.id.conexionAPI -> startActivity(Intent(this, ConexionAPI::class.java))
                R.id.galería -> startActivity(Intent(this, Galeria::class.java))
                R.id.multimedia -> startActivity(Intent(this, Multimedia::class.java))
            }
            true
        }
        popupMenu.show()
    }
}