package com.aplicaciones.appasistencia

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.Adapter.DetallesAsistenciaAdapter
import com.aplicaciones.appasistencia.Datos.DatAsistencia

class DetallesAsistenciaAlumno : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_asistencia_alumno)
        val rvListaAlumnos: RecyclerView = findViewById(R.id.recyclerViewDetallesAsistencia)

        // Obtener el alumnoId y cursoId del Intent
        val alumnoId = intent.getIntExtra("ID_Alumno", -1)
        val cursoId = intent.getIntExtra("ID_Curso", -1)

        // Asegurarse de que ambos valores no sean -1
        if (alumnoId != -1 && cursoId != -1) {
            // Obtener la lista de asistencias de la base de datos
            val datAsistencia = DatAsistencia()
            val listaAlumnos = datAsistencia.obtenerAsistenciaAlumno(alumnoId, cursoId)

            // Crear el adaptador con la lista de alumnos
            val adapter = DetallesAsistenciaAdapter(listaAlumnos)

            // Configurar el RecyclerView
            rvListaAlumnos.layoutManager = LinearLayoutManager(this)
            rvListaAlumnos.adapter = adapter
        } else {
            Log.e("DetallesAlumnoAsistenci", "Faltan los ID de Alumno o Curso")
        }
    }
}
