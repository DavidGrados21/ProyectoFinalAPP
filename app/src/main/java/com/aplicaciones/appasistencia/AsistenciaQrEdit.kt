package com.aplicaciones.appasistencia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aplicaciones.appasistencia.Datos.DatAsistencia
import com.google.zxing.integration.android.IntentIntegrator

class AsistenciaQrEdit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_asistencia_qr_edit)
        // Iniciar la actividad de escaneo de QR
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats (IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el código QR de la Asistencia")
        integrator.setBeepEnabled(true) // Habilita el sonido de beep al escanear
        integrator.setOrientationLocked(false) // Permite la orientación libre
        integrator.initiateScan() // Inicia el escaneo
    }
    // Este método se llama cuando el resultado del escaneo es devuelto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {

                val codigoAsietncia= result.contents
                val alumnoId = intent.getIntExtra("ID_AlumnoId",0) // Leer el ID del alumno
                Log.d("AsistenciaQrEdit", "ID del alumno: $alumnoId")
                Log.d("AsistenciaQrEdit", "Código de asistencia: $codigoAsietncia")
                 val datAsistencia = DatAsistencia()
                 val resultado = datAsistencia.actualizarAsistencia(alumnoId, codigoAsietncia.toInt())
                 Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
                onBackPressed() // Regresar a la actividad anterior

            } else {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
                onBackPressed() // Regresar a la actividad anterior
            }
        }
    }
}