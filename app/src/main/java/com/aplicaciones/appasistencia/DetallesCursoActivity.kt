package com.aplicaciones.appasistencia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aplicaciones.appasistencia.Datos.DatAlumnoCurso
import com.aplicaciones.appasistencia.Datos.DatCurso

class DetallesCursoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_curso)

        val codigoCurso = intent.getStringExtra("ID_CURSO")?.toIntOrNull() ?: 0

        // Buscar el curso en la base de datos
        val curso = DatCurso().obtenerCursoConProfesorPorId(codigoCurso)

        // Mostrar los detalles en la interfaz
        val tvNombreCurso: TextView = findViewById(R.id.tvNombreCursoDetalle)
        val tvCodigoCurso: TextView = findViewById(R.id.tvCodigoCursoDetalle)
        val tvHorario: TextView = findViewById(R.id.tvHorarioDetalle)
        val tvDiaSemana: TextView = findViewById(R.id.tvDiaSemanaDetalle)
        val tvProfesor: TextView = findViewById(R.id.tvProfesor)

        if (curso != null) {

            tvNombreCurso.text = curso.nombreCurso
            tvCodigoCurso.text = curso.codigoCurso
            tvHorario.text = "${curso.horaInicio} - ${curso.horaFin}"
            tvDiaSemana.text = curso.diaSemana
            tvProfesor.text = curso.nombreProfesor
        } else {
            tvNombreCurso.text = "Curso no encontrado"
        }

        // Obtener referencia al botón y asignar el evento OnClick
        val btnRegistrarAlumnoCurso: Button = findViewById(R.id.btnAgregarCurso)
        btnRegistrarAlumnoCurso.setOnClickListener {
            // Obtener el ID del alumno desde el Intent
            val alumnoId = intent.getIntExtra("ID_AlumnoQR",-1)
            Log.d("DetallesCursoActivity", "ID del alumno: $alumnoId")
            if (curso != null) {
                // Registrar al alumno en el curso
                registrarAlumnoCurso(alumnoId, curso.id)
            }
        }
        // Botón para volver atrás
        val btnVolverAtras: Button = findViewById(R.id.btnEscanearQR)
        btnVolverAtras.setOnClickListener {
            volverAtras()
        }
    }

    // Método para registrar al alumno en el curso
    private fun registrarAlumnoCurso(alumnoID: Int, cursoID: Int) {
        // Llamamos al método de registro
        val mensaje = DatAlumnoCurso().registrarAlumnoCurso(alumnoID, cursoID)

        // Mostrar el mensaje correspondiente (éxito o error)
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

        // Si el registro fue exitoso, redirigir a otra actividad
        if (mensaje.contains("exitosamente", ignoreCase = true)) {
            // Crear el Intent para redirigir a otra actividad
            val Alumno = intent.getIntExtra("ID_AlumnoQR", -1)

            volverAtras()
        }
    }
    // Método para volver atrás
    private fun volverAtras() {
        finish() // Simula la acción de retroceder sin cerrar actividades adicionales
    }
}
