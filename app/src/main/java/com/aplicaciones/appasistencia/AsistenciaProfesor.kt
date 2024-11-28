package com.aplicaciones.appasistencia

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.Adapter.AsistenciaAdapter
import com.aplicaciones.appasistencia.Datos.DatAsistencia

class AsistenciaProfesor : AppCompatActivity() {

    private lateinit var recyclerViewAsistencias: RecyclerView
    private var idCurso: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_asistencia_profesor)

        // Obtener datos del Intent
        idCurso = intent.getIntExtra("ID_Curso", -1)
        val idProfesor = intent.getIntExtra("ID_Profesor", -1)
        Log.d("AsistenciaProfesor", "ID del Curso: $idCurso")

        // Mostrar información del curso
        val tvCursoInfo: TextView = findViewById(R.id.tvCursoInfo)

        // Configurar RecyclerView
        recyclerViewAsistencias = findViewById(R.id.recyclerViewAsistencias)
        recyclerViewAsistencias.layoutManager = LinearLayoutManager(this)

        // Listar asistencias
        listarAsistencias()

        // Configurar el botón para registrar asistencia
        val btnRegistrarAsistencia: Button = findViewById(R.id.btnRegistrarAsistencia)
        btnRegistrarAsistencia.setOnClickListener {
            registrarAsistencia(idProfesor)
        }
    }

    private fun listarAsistencias() {
        if (idCurso != -1) {
            val datAsistencia = DatAsistencia()
            val listaAsistencias = datAsistencia.listarAsistenciasPorCurso(idCurso)
            Log.d("AsistenciaProfesor", "Lista de Asistencias: $listaAsistencias")

            val adapter = AsistenciaAdapter(listaAsistencias)
            recyclerViewAsistencias.adapter = adapter
        } else {
            Toast.makeText(this, "Curso no válido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registrarAsistencia(idProfesor: Int) {
        if (idCurso != -1 && idProfesor != -1) {
            val datAsistencia = DatAsistencia()
            val resultado = datAsistencia.registrarAsistenciaPorCurso(idCurso)

            Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()

            // Actualizar la lista de asistencias después de registrar
            listarAsistencias()
        } else {
            Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
        }
    }
}
