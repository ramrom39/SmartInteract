package com.example.smartinteract

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ConexionAPI : AppCompatActivity() {

    private lateinit var nombre: TextView
    private lateinit var usuario: TextView
    private lateinit var contraseña: TextView
    private lateinit var paises: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conexionapi)

        // Inicializamos los campos de texto
        nombre = findViewById(R.id.textViewAPI)
        usuario = findViewById(R.id.textViewAPI2)
        contraseña = findViewById(R.id.textViewAPI3)
        paises = findViewById(R.id.textViewAPI4)

        // Llamamos a la función que obtiene los datos de la API
        obtenerDatosUsuario()



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

    private fun obtenerDatosUsuario() {
        // Creamos una Coroutine para hacer la petición en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://randomuser.me/api/") // URL de la API
                val urlConnection = url.openConnection() as HttpURLConnection
                val response = StringBuilder()

                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                val inputStream = urlConnection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?

                // Leemos la respuesta de la API
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                // Procesamos el JSON de la respuesta
                val respuestaJson = response.toString()
                val jsonObject = JSONObject(respuestaJson)

                // Accedemos al array 'results' en el JSON
                val resultsArray = jsonObject.getJSONArray("results")
                val firstResult = resultsArray.getJSONObject(0)

                // Accedemos al objeto 'name' para obtener el nombre completo
                val nameObject = firstResult.getJSONObject("name")
                val firstName = nameObject.getString("first")
                val lastName = nameObject.getString("last")
                val fullName = "$firstName $lastName"

                // Accedemos al objeto 'login' para obtener el username y password
                val loginObject = firstResult.getJSONObject("login")
                val username = loginObject.getString("username")
                val password = loginObject.getString("password")

                // Accedemos al objeto 'location' para obtener el país
                val locationObject = firstResult.getJSONObject("location")
                val country = locationObject.getString("country")

                // Actualizamos la UI con los datos obtenidos
                withContext(Dispatchers.Main) {
                    nombre.text = "Nombre: $fullName"
                    usuario.text="Usuario: $username"
                    contraseña.text="Contraseña: $password"
                    paises.text="País: $country"

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Si ocurre un error, mostramos un mensaje
                    Toast.makeText(
                        this@ConexionAPI,
                        "Error al obtener los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
