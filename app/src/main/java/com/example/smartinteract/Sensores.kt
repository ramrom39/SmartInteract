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
    private var proximidadValorTextView: TextView? = null // No lo declaramos como lateinit, lo inicializamos cuando sea necesario.

    private var giroscopioSensor: Sensor?=null
    private var giroscopioValorTextView: TextView? = null

    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sensores)

        // Inicializamos los botones de la actividad principal
        val giroscopio= findViewById<Button>(R.id.giroscopio)
        val proximidad = findViewById<Button>(R.id.proximidad)


        // Inicializamos el SensorManager y el sensor de proximidad
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximidadSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        giroscopioSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        proximidad.setOnClickListener {
            // Inflamos el AlertDialog cuando se presiona el botón de proximidad
            showProximitySensorDialog()
        }
        giroscopio.setOnClickListener {
            // Inflamos el AlertDialog cuando se presiona el botón de giroscopio
            showGyroscopeDialog()
        }
    }

    private fun showProximitySensorDialog() {
        // Inflamos el layout personalizado para el AlertDialog
        val dialogBuilder = LayoutInflater.from(this)
        val dialogView = dialogBuilder.inflate(R.layout.dialog_layout, null)

        // Inicializamos proximidadValorTextView dentro de la función para asegurarnos que está disponible
        proximidadValorTextView = dialogView.findViewById(R.id.proximidadValor)
        val cerrarButton = dialogView.findViewById<Button>(R.id.cerrarButton)

        // Creamos el AlertDialog
        alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configuramos el botón de cerrar para que cierre el dialog
        cerrarButton.setOnClickListener {
            alertDialog?.dismiss() // Cierra el dialog
        }

        // Mostramos el AlertDialog
        alertDialog?.show()
    }

    private fun showGyroscopeDialog() {
        // Inflamos el layout personalizado para el AlertDialog
        val dialogBuilder = LayoutInflater.from(this)
        val dialogView = dialogBuilder.inflate(R.layout.dialog_layout, null)

        // Inicializamos proximidadValorTextView dentro de la función para asegurarnos que está disponible
        giroscopioValorTextView = dialogView.findViewById(R.id.proximidadValor)
        val cerrarButton = dialogView.findViewById<Button>(R.id.cerrarButton)

        // Creamos el AlertDialog
        alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configuramos el botón de cerrar para que cierre el dialog
        cerrarButton.setOnClickListener {
            alertDialog?.dismiss() // Cierra el dialog
        }

        // Mostramos el AlertDialog
        alertDialog?.show()
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_PROXIMITY -> {
                    val distancia = event.values[0]
                    if (alertDialog?.isShowing == true) {
                        proximidadValorTextView?.text = "Distancia: $distancia cm"
                    }
                    if (distancia == 0.0f) {
                        alertDialog?.dismiss()
                    }
                }
                Sensor.TYPE_GYROSCOPE -> {
                    val rotacionX = event.values[0] // Rotación en el eje X
                    val rotacionY = event.values[1] // Rotación en el eje Y
                    val rotacionZ = event.values[2] // Rotación en el eje Z

                    giroscopioValorTextView?.text = "Rotación\nX: $rotacionX\nY: $rotacionY\nZ: $rotacionZ"
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
            // Registramos el Listener para el sensor de proximidad
            sensorManager.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_UI)
        }
        giroscopioSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Desregistramos el Listener para ahorrar batería
        sensorManager.unregisterListener(this)
    }
}