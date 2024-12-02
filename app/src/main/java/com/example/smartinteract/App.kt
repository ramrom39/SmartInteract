package com.example.smartinteract

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class App : AppCompatActivity() {
    // Declaramos las variables a nivel de clase para que puedan ser accedidas desde cualquier parte del código
    private var Dialog: AlertDialog? = null
    private var textview:TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)

    // Inicializamos las variables que se utilizarán en el código

        val menu = findViewById<ImageButton>(R.id.buttonPopupMenu)
        val home= findViewById<ImageButton>(R.id.menu_home)
        val sensores = findViewById<ImageButton>(R.id.menu_sensores)
        val conexionAPI = findViewById<ImageButton>(R.id.menu_API)
        val galeria = findViewById<ImageButton>(R.id.menu_imagenes)
        val multimedia = findViewById<ImageButton>(R.id.menu_multimedia)
        val botonflotante=findViewById<FloatingActionButton>(R.id.floatingActionButton)

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
        // Método que activa el botón flotante con la animación de crecimiento y reducción
        botonflotante.setOnClickListener {
            // Animación de escala (cambiar tamaño)
            val escalarX = ObjectAnimator.ofFloat(it, "scaleX", 1.2f).apply { duration = 150 }
            val escalarY = ObjectAnimator.ofFloat(it, "scaleY", 1.2f).apply { duration = 150 }

            // Animación de reducción (volver al tamaño original)
            val desescalarX = ObjectAnimator.ofFloat(it, "scaleX", 1f).apply { duration = 150 }
            val desescalarY = ObjectAnimator.ofFloat(it, "scaleY", 1f).apply { duration = 150 }

            // Inicia la animación de aumento (crecimiento)
            escalarX.start()
            escalarY.start()

            // Después de la animación de aumento, inicia la de reducción: addlistener para escuchar cuando escalarX termine
            escalarX.addListener(object : AnimatorListenerAdapter() {
                //Override onAnimationEnd para ejecutar la animación de reducción
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    desescalarX.start()
                    desescalarY.start()

                    // Muestra el diálogo de inflado después de que termine la animación
                    layoutInfladoBotonFlotante()
                }
            })
        }
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

// Método que infla el layout personalizado para el AlertDialog

    private fun layoutInfladoBotonFlotante() {
        // Inflamos el layout personalizado para el AlertDialog
        val dialogBuilder = LayoutInflater.from(this)
        val dialogView = dialogBuilder.inflate(R.layout.dialog_layout, null)

        // Inicializamos proximidadValorTextView dentro de la función para asegurarnos que está disponible
        textview = dialogView.findViewById(R.id.proximidadValor)
        val cerrarButton = dialogView.findViewById<Button>(R.id.cerrarButton)


        val deviceInfo = """
        Modelo: ${Build.MODEL}
        Fabricante: ${Build.MANUFACTURER}
        Marca: ${Build.BRAND}
        Versión de Android: ${Build.VERSION.RELEASE}
        SDK: ${Build.VERSION.SDK_INT}
        """.trimIndent()

        // Establecemos la información del dispositivo en el TextView
        textview?.text = deviceInfo

        // Creamos el AlertDialog
        Dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configuramos el botón de cerrar para que cierre el dialog
        cerrarButton.setOnClickListener {
            Dialog?.dismiss()
        }

        // Mostramos el AlertDialog
        Dialog?.show()
    }
}