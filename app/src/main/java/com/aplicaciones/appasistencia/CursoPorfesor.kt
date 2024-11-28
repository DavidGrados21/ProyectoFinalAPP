package com.aplicaciones.appasistencia

import android.content.Intent
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.Datos.DatCurso
import com.aplicaciones.appasistencia.Adapters.CursoAdapter

class CursoPorfesor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_curso_porfesor)
        // Inicializar el RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.rvCursos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val profesorId = intent.getIntExtra("ID_Profesor", -1)
        val cursos = DatCurso().obtenerCursosPorProfesorId(profesorId)

        // Configurar el Adapter
        val adapter = CursoAdapter(cursos)
        recyclerView.adapter = adapter
    }
    override fun onResume() {
        super.onResume()
        // Recargar los cursos cada vez que la actividad sea visible
        cargarCursos()
    }
    private fun cargarCursos() {
        val profesorId = intent.getIntExtra("ID_Profesor", -1)
        val cursos = DatCurso().obtenerCursosPorProfesorId(profesorId)

        // Configurar el Adapter
        val adapter = CursoAdapter(cursos)
        val recyclerView: RecyclerView = findViewById(R.id.rvCursos)
        recyclerView.adapter = adapter
    }
    // Este es el método que se llamará cuando se haga clic en el botón
    fun abrirRegistrarCurso(view: android.view.View) {
        // Obtener el ID del profesor desde la actividad anterior
        val profesorId = intent.getIntExtra("ID_Profesor", -1)

        // Crear el Intent para abrir la actividad RegistrarAlumno
        val intent = Intent(this, Registrar_Curso::class.java)

        // Pasar el ID del profesor si es necesario
        intent.putExtra("ID_Profesor", profesorId)

        // Iniciar la nueva actividad
        startActivity(intent)
    }
}