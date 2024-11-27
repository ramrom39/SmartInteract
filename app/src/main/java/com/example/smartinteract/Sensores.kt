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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sensores)

        // Inicializamos los botones de la actividad principal
        val proximidad = findViewById<Button>(R.id.proximidad)

        // Inicializamos el SensorManager y el sensor de proximidad
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximidadSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        proximidad.setOnClickListener {
            // Inflamos el AlertDialog cuando se presiona el botón de proximidad
            showProximitySensorDialog()
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
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Configuramos el botón de cerrar para que cierre el dialog
        cerrarButton.setOnClickListener {
            alertDialog.dismiss() // Cierra el dialog
        }

        // Mostramos el AlertDialog
        alertDialog.show()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_PROXIMITY) {
            val distancia = event.values[0] // Obtiene la distancia medida por el sensor de proximidad
            // Verificamos que proximidadValorTextView esté inicializado antes de actualizar el texto
            proximidadValorTextView?.text = "Distancia: $distancia cm"  // Actualiza el TextView con el valor
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
    }

    override fun onPause() {
        super.onPause()
        // Desregistramos el Listener para ahorrar batería
        sensorManager.unregisterListener(this)
    }
}