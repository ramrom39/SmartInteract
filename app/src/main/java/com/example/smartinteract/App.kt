package com.example.smartinteract

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

class App : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)

        val menu = findViewById<ImageButton>(R.id.buttonPopupMenu)


        val home= findViewById<ImageButton>(R.id.menu_home)
        val sensores = findViewById<ImageButton>(R.id.menu_sensores)
        val conexionAPI = findViewById<ImageButton>(R.id.menu_API)
        val galeria = findViewById<ImageButton>(R.id.menu_imagenes)
        val multimedia = findViewById<ImageButton>(R.id.menu_multimedia)



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