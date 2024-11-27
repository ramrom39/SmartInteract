package com.example.smartinteract

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Sensores : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private var proximidadSensor: Sensor? = null
    private var proximidadValorTextView: TextView? = null

    private var giroscopioSensor: Sensor?=null
    private var giroscopioValorTextView: TextView? = null


    private var luzSensor: Sensor?=null
    private var luzValorSensor: TextView? = null


    private var aceletrometroSensor: Sensor?=null
    private var acelerometroValorTextView: TextView? = null

    private var proximidadDialog: AlertDialog? = null
    private var giroscopioDialog: AlertDialog? = null
    private var luzDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sensores)

        // Inicializamos los botones de la actividad principal
        val giroscopio= findViewById<Button>(R.id.giroscopio)
        val proximidad = findViewById<Button>(R.id.proximidad)
        val luz=findViewById<Button>(R.id.luz)
        val acelerometro=findViewById<Button>(R.id.acelerometro)


        // Inicializamos el SensorManager y el sensor de proximidad
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximidadSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        giroscopioSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        luzSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        proximidad.setOnClickListener {
            // Inflamos el AlertDialog cuando se presiona el botón de proximidad
            showProximitySensorDialog()
        }
        giroscopio.setOnClickListener {
            // Inflamos el AlertDialog cuando se presiona el botón de giroscopio
            showGyroscopeDialog()
        }
        luz.setOnClickListener {
            // Inflamos el AlertDialog cuando se presiona el botón de luz
            showLightSensorDialog()
        }
//        acelerometro.setOnClickListener {
//            // Inflamos el AlertDialog cuando se presiona el botón de acelerometro
//            showAcelerometroDialog()
//        }
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
    }

    override fun onPause() {
        super.onPause()
        // Desregistramos el Listener para ahorrar batería
        sensorManager.unregisterListener(this)
    }
}