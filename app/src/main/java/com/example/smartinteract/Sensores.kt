package com.example.smartinteract

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

class Sensores : AppCompatActivity(), SensorEventListener {

    private lateinit var gestureDetector: GestureDetectorCompat

    private lateinit var sensorManager: SensorManager

    private var proximidadSensor: Sensor? = null
    private var proximidadValorTextView: TextView? = null

    private var giroscopioSensor: Sensor?=null
    private var giroscopioValorTextView: TextView? = null


    private var luzSensor: Sensor?=null
    private var luzValorSensor: TextView? = null


    private var acelerometroSensor: Sensor?=null
    private var acelerometroValorTextView: TextView? = null

    private var proximidadDialog: AlertDialog? = null
    private var giroscopioDialog: AlertDialog? = null
    private var luzDialog: AlertDialog? = null
    private var acelerometroDialog: AlertDialog? = null


    private var contador = 0
    private var lastShakeTime = 0L
    private val SHAKE_TIME_INTERVAL = 500L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sensores)

        // Inicializamos los botones de la actividad principal
        val giroscopio= findViewById<Button>(R.id.giroscopio)
        val proximidad = findViewById<Button>(R.id.proximidad)
        val luz=findViewById<Button>(R.id.luz)
        val acelerometro=findViewById<Button>(R.id.acelerometro)



        val menu = findViewById<ImageButton>(R.id.buttonPopupMenu)


        val home= findViewById<ImageButton>(R.id.menu_home)
        val sensores = findViewById<ImageButton>(R.id.menu_sensores)
        val conexionAPI = findViewById<ImageButton>(R.id.menu_API)
        val galeria = findViewById<ImageButton>(R.id.menu_imagenes)
        val multimedia = findViewById<ImageButton>(R.id.menu_multimedia)

        gestureDetector = GestureDetectorCompat(this, GestureListener(this))


        // Inicializamos el SensorManager y los sensores
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        proximidadSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        giroscopioSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        luzSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        acelerometroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


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
        proximidad.setOnClickListener {
            showProximitySensorDialog()
        }
        giroscopio.setOnClickListener {
            showGyroscopeDialog()
        }
        luz.setOnClickListener {
            showLightSensorDialog()
        }
        acelerometro.setOnClickListener {
            showAcelerometroDialog()
        }
    }


    inner class GestureListener(val context: Sensores) : GestureDetector.SimpleOnGestureListener() {
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
    private fun showProximitySensorDialog() {
        // Inflamos el layout personalizado para el AlertDialog
        val dialogBuilder = LayoutInflater.from(this)
        val dialogView = dialogBuilder.inflate(R.layout.dialog_layout, null)

        // Inicializamos proximidadValorTextView dentro de la función para asegurarnos que está disponible
        proximidadValorTextView = dialogView.findViewById(R.id.proximidadValor)
        val cerrarButton = dialogView.findViewById<Button>(R.id.cerrarButton)

        // Creamos el AlertDialog
        proximidadDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configuramos el botón de cerrar para que cierre el dialog
        cerrarButton.setOnClickListener {
            proximidadDialog?.dismiss() // Cierra el dialog
        }

        // Mostramos el AlertDialog
        proximidadDialog?.show()
    }

    private fun showGyroscopeDialog() {
        // Inflamos el layout personalizado para el AlertDialog
        val dialogBuilder = LayoutInflater.from(this)
        val dialogView = dialogBuilder.inflate(R.layout.dialog_layout, null)

        // Inicializamos proximidadValorTextView dentro de la función para asegurarnos que está disponible
        giroscopioValorTextView = dialogView.findViewById(R.id.proximidadValor)
        val cerrarButton = dialogView.findViewById<Button>(R.id.cerrarButton)

        // Creamos el AlertDialog
        giroscopioDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configuramos el botón de cerrar para que cierre el dialog
        cerrarButton.setOnClickListener {
            giroscopioDialog?.dismiss() // Cierra el dialog
        }

        // Mostramos el AlertDialog
        giroscopioDialog?.show()
    }
    private fun showLightSensorDialog() {
        // Inflamos el layout personalizado para el AlertDialog
        val dialogBuilder = LayoutInflater.from(this)
        val dialogView = dialogBuilder.inflate(R.layout.dialog_layout, null)


        // Inicializamos proximidadValorTextView dentro de la función para asegurarnos que está disponible
        luzValorSensor = dialogView.findViewById(R.id.proximidadValor)
        val cerrarButton = dialogView.findViewById<Button>(R.id.cerrarButton)

        // Creamos el AlertDialog
        luzDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configuramos el botón de cerrar para que cierre el dialog
        cerrarButton.setOnClickListener {
            luzDialog?.dismiss() // Cierra el dialog
        }

        // Mostramos el AlertDialog
        luzDialog?.show()
    }
    private fun showAcelerometroDialog() {
        // Inflamos el layout personalizado para el AlertDialog
        val dialogBuilder = LayoutInflater.from(this)
        val dialogView = dialogBuilder.inflate(R.layout.dialog_layout, null)

        // Inicializamos proximidadValorTextView dentro de la función para asegurarnos que está disponible

        acelerometroValorTextView = dialogView.findViewById(R.id.proximidadValor)
        val cerrarButton = dialogView.findViewById<Button>(R.id.cerrarButton)

        // Creamos el AlertDialog
        acelerometroDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configuramos el botón de cerrar para que cierre el dialog
        cerrarButton.setOnClickListener {
            acelerometroDialog?.dismiss() // Cierra el dialog
        }

        // Mostramos el AlertDialog
        acelerometroDialog?.show()
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_PROXIMITY -> {
                    val distancia = event.values[0]
                    if (proximidadDialog?.isShowing == true) {
                        proximidadValorTextView?.text = "Distancia: $distancia cm"
                    }
                    if (distancia == 0.0f) {
                        proximidadDialog?.dismiss()
                    }
                }
                Sensor.TYPE_GYROSCOPE -> {
                    val rotacionX = event.values[0]
                    val rotacionY = event.values[1]
                    val rotacionZ = event.values[2]
                    if (giroscopioDialog?.isShowing == true) {
                        giroscopioValorTextView?.text = "Rotación\nX: $rotacionX\nY: $rotacionY\nZ: $rotacionZ"
                    }
                }
                Sensor.TYPE_LIGHT -> {
                    val luz = event.values[0]
                    if (luzDialog?.isShowing == true) {
                        luzValorSensor?.text = "Luz: $luz Lux"
                    }
                }

                Sensor.TYPE_ACCELEROMETER -> {
                    val aceleracionX = event.values[0]
                    val aceleracionY = event.values[1]
                    val aceleracionZ = event.values[2]

                    // Calcular la magnitud de la aceleración total
                    val aceleracionTotal = Math.sqrt((aceleracionX * aceleracionX + aceleracionY * aceleracionY + aceleracionZ * aceleracionZ).toDouble()
                    )

                    // Umbral para detectar una agitación (ajústalo según tu dispositivo)
                    val UMBRAL_AGITACION = 15.0  // Ajusta este valor según tus necesidades

                    // Verificar si la aceleración total supera el umbral
                    if (aceleracionTotal > UMBRAL_AGITACION) {
                        val currentTime = System.currentTimeMillis()

                        // Solo contar si ha pasado suficiente tiempo desde la última agitación
                        if (currentTime - lastShakeTime > SHAKE_TIME_INTERVAL) {
                            contador += 1
                            lastShakeTime = currentTime  // Actualizar el tiempo de la última agitación
                        }
                    }

                    if (acelerometroDialog?.isShowing == true) {
                        acelerometroValorTextView?.text = "Agitaciones detectadas: $contador"
                    }
                }

            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Aquí puedes manejar los cambios de precisión si es necesario
    }

    override fun onResume() {
        super.onResume()
        proximidadSensor?.also { proxSensor ->
            sensorManager.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_UI)
        }
        giroscopioSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        luzSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        acelerometroSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Desregistramos el Listener para ahorrar batería
        sensorManager.unregisterListener(this)
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