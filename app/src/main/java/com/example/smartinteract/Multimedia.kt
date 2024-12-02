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
    // Declaramos las variables a nivel de clase para que puedan ser accedidas desde cualquier parte del código
    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.multimedia)

        // Inicializamos las variables y botones que vamos a utilizar

        val botonSonido = findViewById<ImageButton>(R.id.reproducir)
        val botonpausar = findViewById<ImageButton>(R.id.parar)
        val botonReiniciar = findViewById<ImageButton>(R.id.reiniciar)
        val home= findViewById<ImageButton>(R.id.menu_home)
        val sensores = findViewById<ImageButton>(R.id.menu_sensores)
        val conexionAPI = findViewById<ImageButton>(R.id.menu_API)
        val galeria = findViewById<ImageButton>(R.id.menu_imagenes)
        val multimedia = findViewById<ImageButton>(R.id.menu_multimedia)
        val menu = findViewById<ImageButton>(R.id.buttonPopupMenu)
        val mediaPlayer = MediaPlayer.create(this, R.raw.gigachad)


        // Crear una instancia de GestureDetector

        gestureDetector = GestureDetectorCompat(this, GestureListener(this))

        // Método que lanza el menu de opciones
        menu.setOnClickListener {
            showPopupMenu(it)
        }
        // Método que lanza la actividad de la pantalla de inicio
        home.setOnClickListener {
            val intent = Intent(this, App::class.java)
            startActivity(intent)
        }
        // Método que lanza la actividad de la pantalla de sensores
        sensores.setOnClickListener {
            val intent = Intent(this, Sensores::class.java)
            startActivity(intent)
        }
        // Método que lanza la actividad de la pantalla de conexión a la API
        conexionAPI.setOnClickListener {
            val intent = Intent(this, ConexionAPI::class.java)
            startActivity(intent)
        }
        // Método que lanza la actividad de la pantalla de la galería
        galeria.setOnClickListener {
            val intent = Intent(this, Galeria::class.java)
            startActivity(intent)
        }
        // Método que lanza la actividad de la pantalla de multimedia
        multimedia.setOnClickListener {
            val intent = Intent(this, Multimedia::class.java)
            startActivity(intent)
        }

        // Método que reproduce el sonido
        botonSonido.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.seekTo(0) // Reiniciar al principio
            } else {
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            }
        }
        // Método que pausa el sonido
        botonpausar.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start() // Start siempre apunta por defecto al segundo donde se paro para continuar. Si quieres modificarlo: seek
            }
        }
        // Método que reinicia el sonido
        botonReiniciar.setOnClickListener {
            mediaPlayer.seekTo(0)
        }
    }

    // Funcion para el gesto de deslizar hacia la izquierda

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

    // Función para manejar eventos de toque en la pantalla

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Utilizamos gestureDetector para manejar el evento
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    // Función auxiliar para mostrar mensajes en pantalla

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    // Función para mostrar el menu desplegable

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