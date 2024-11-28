package com.aplicaciones.appasistencia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.util.Log
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator

class AgregarCursoQr : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_curso_qr)

        // Iniciar la actividad de escaneo de QR
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats (IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el código QR del curso")
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

                val codigoCurso = result.contents
                val alumnoId = intent.getIntExtra("ID_AlumnoCurso",0) // Leer el ID del alumno

                // Navegar a la actividad de detalles con el código QR
                val intent = Intent(this@AgregarCursoQr, DetallesCursoActivity::class.java)

                intent.putExtra("ID_CURSO", codigoCurso)
                intent.putExtra("ID_AlumnoQR", alumnoId) // Pasar el ID del alumno
                Log.d("AgregarCursoQr", "ID del alumno: $alumnoId")
                startActivity(intent)
                finish() // Finalizar esta actividad
            } else {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
                onBackPressed() // Regresar a la actividad anterior
            }
        }
    }


}
