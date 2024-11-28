package com.aplicaciones.appasistencia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.Adapter.CursoAdapterAlumno
import com.aplicaciones.appasistencia.Datos.DatCurso
import com.aplicaciones.appasistencia.R

class CursoAlumno : AppCompatActivity() {
    private lateinit var rvCursos: RecyclerView
    private var alumnoId: Int = -1
    private lateinit var adapter: CursoAdapterAlumno

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curso_alumno)

        // Configuración del botón Añadir Curso
        val btnAñadirCurso: Button = findViewById(R.id.btnAñadirCurso)
        alumnoId = intent.getIntExtra("ID_AlumnoId", -1)
        intent.putExtra("ID_Alumno", alumnoId)
        Log.d("CursoAlumno", "ID del alumno: $alumnoId")

        btnAñadirCurso.setOnClickListener {
            val intent = Intent(this, AgregarCursoQr::class.java)
            intent.putExtra("ID_AlumnoCurso", alumnoId) // Pasar el ID del alumno
            Log.d("CursoAlumno", "ID del alumno: $alumnoId")
            startActivity(intent)
        }
        val sharedPreferences = getSharedPreferences("AppAsistencia", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("ID_Alumno", alumnoId)
        editor.apply()

        // Configuración inicial del RecyclerView
        rvCursos = findViewById(R.id.rvCursos)
        rvCursos.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador vacío
        adapter = CursoAdapterAlumno(emptyList(), this)  // Pasa el contexto
        rvCursos.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        // Actualizar los datos del RecyclerView al volver a la actividad
        actualizarCursos()
    }

    private fun actualizarCursos() {
        val datCurso = DatCurso()
        val cursos = datCurso.obtenerCursosParaAlumno(alumnoId)
        adapter.cursos = cursos // Actualizar los datos en el adaptador
        adapter.notifyDataSetChanged() // Notificar al adaptador que los datos cambiaron
    }
}
