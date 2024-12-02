package com.example.smartinteract

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import java.io.OutputStream

class Galeria : AppCompatActivity() {

    // Declaramos las variables a nivel de clase para que puedan ser accedidas desde cualquier parte del código
    private lateinit var gestureDetector: GestureDetectorCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.galeria)

        // Inicializamos las variables
        val boton = findViewById<Button>(R.id.cargarImagen)
        val home= findViewById<ImageButton>(R.id.menu_home)
        val sensores = findViewById<ImageButton>(R.id.menu_sensores)
        val conexionAPI = findViewById<ImageButton>(R.id.menu_API)
        val galeria = findViewById<ImageButton>(R.id.menu_imagenes)
        val multimedia = findViewById<ImageButton>(R.id.menu_multimedia)
        val menu = findViewById<ImageButton>(R.id.buttonPopupMenu)

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
        // Método que lanza la actividad de la pantalla de carga de imagen
        boton.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }
    // Funcion para el gesto de deslizar hacia la izquierda
    inner class GestureListener(val context: Galeria) : GestureDetector.SimpleOnGestureListener() {
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

    // Función auxiliar para mostrar mensajes en pantalla

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Utilizamos gestureDetector para manejar el evento
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
    // Función para manejar eventos de toque en la pantalla

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    // Metodo para registrar la actividad de la galería. La imagen se convierte a bitmap y se muestra en el imageview. Además se guardará en la galería
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            val imageBitmap = intent?.extras?.get("data") as? Bitmap
            val imageView = findViewById<ImageView>(R.id.imageButton)

            if (imageBitmap != null) {
                imageView.setImageBitmap(imageBitmap)
                saveImageToGallery(imageBitmap)  // Guardar la imagen en la galería
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background)
            }

        } else {
            Toast.makeText(this, "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para guardar la imagen en la galería

    private fun saveImageToGallery(bitmap: Bitmap) {
        // Crear los metadatos necesarios para registrar la imagen en la galería
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "foto_${System.currentTimeMillis()}.jpg") // Nombre del archivo de la imagen
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Tipo de archivo (JPEG en este caso)
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SmartInteract") // Carpeta donde se guardará la imagen en la galería
        }

        // Abrimos un flujo de comunicación en la galería para registrar la imagen. IMAGEURI SE CONVIERTE EN UN CONTENEDOR DE DATOS
        val contentResolver = contentResolver
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // Si la URI fue creada con éxito, podemos escribir la imagen. EN EL CONTENEDOR DE DATOS SE GUARDA LA IMAGEN
        imageUri?.let { uri ->
            var outputStream: OutputStream? = null
            try {
                // Abrimos un flujo de salida hacia el archivo de la galería
                outputStream = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    // Insertamos el contenido del Bitmap en la ubicación del flujo
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    // Si to-do va bien, mostramos un mensaje de éxito
                    Toast.makeText(this, "Imagen guardada en la galería", Toast.LENGTH_SHORT).show()
                } else {
                    // Si no se pudo abrir el flujo, informamos el error
                    Toast.makeText(this, "Error al abrir el flujo de salida", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Capturamos cualquier excepción durante el proceso de escritura y mostramos un error
                e.printStackTrace()
                Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
            } finally {
                // Cerramos el flujo para liberar los recursos
                outputStream?.close()
            }
        } ?: run {
            // Si no se pudo obtener una URI válida, mostramos un mensaje de error
            Toast.makeText(this, "Error al obtener URI para la imagen", Toast.LENGTH_SHORT).show()
        }
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
