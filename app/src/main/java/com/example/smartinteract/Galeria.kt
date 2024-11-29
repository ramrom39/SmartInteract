package com.example.smartinteract

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStream

class Galeria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.galeria)

        val boton = findViewById<Button>(R.id.cargarImagen)

        boton.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }

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

}
