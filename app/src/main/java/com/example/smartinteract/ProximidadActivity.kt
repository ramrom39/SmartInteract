package com.example.smartinteract

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProximidadActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var proximidadSensor: Sensor? = null
    private lateinit var proximidadValorTextView: TextView
    private lateinit var cerrarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_layout)

        // Inicializamos los elementos del layout
        proximidadValorTextView = findViewById(R.id.proximidadValor)
        cerrarButton = findViewById(R.id.cerrarButton)

        // Inicializamos el SensorManager y el sensor de proximidad
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximidadSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        // Configuramos el botón para cerrar la actividad
        cerrarButton.setOnClickListener {
            finish()  // Cierra la actividad actual
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_PROXIMITY) {
            val distancia = event.values[0] // Obtiene la distancia del sensor
            proximidadValorTextView.text = "Distancia: $distancia cm"  // Actualiza el TextView con el valor
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
