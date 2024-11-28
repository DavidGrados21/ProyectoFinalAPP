package com.aplicaciones.appasistencia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuRigistro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_rigistro)
        val btnProfesor = findViewById<Button>(R.id.btnProfesor)
        val btnAlumno = findViewById<Button>(R.id.btnAlumno)

        // Configurar acción del botón Profesor
        btnProfesor.setOnClickListener {
            // Iniciar la actividad para registrar un profesor
            val intent = Intent(this, Registrar_Profesor::class.java)
            startActivity(intent)
        }

        // Configurar acción del botón Alumno
        btnAlumno.setOnClickListener {
            // Iniciar la actividad para registrar un alumno
            val intent = Intent(this, Registrar_Alumno::class.java)
            startActivity(intent)
        }
    }

}