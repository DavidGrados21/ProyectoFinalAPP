package com.aplicaciones.appasistencia

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.Datos.DatAlumno
import com.aplicaciones.appasistencia.Entidad.Alumno

class DetallesAlumnosCurso : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var alumnoAdapter: AlumnoAdapter
    private var listaAlumnos: List<Alumno> = emptyList()
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Asegura que el contenido se muestre sin bordes
        setContentView(R.layout.activity_detalles_alumnos_curso)

        recyclerView = findViewById(R.id.recyclerViewAlumnos)
        searchEditText = findViewById(R.id.searchEditText)

        val cursoId = intent.getIntExtra("ID_Curso", -1)  // Obtener el cursoId desde el Intent
        Log.d("DetallesAlumnosCurso", "Curso ID recibido: $cursoId")

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador con la lista vacía
        alumnoAdapter = AlumnoAdapter(listaAlumnos)
        recyclerView.adapter = alumnoAdapter

        // Verificar si se ha recibido un cursoId válido
        if (cursoId != -1) {
            obtenerAlumnosPorCurso(cursoId)
        }

        // Agregar un TextWatcher al EditText para filtrar la lista de alumnos
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val query = charSequence.toString()
                filtrarAlumnos(query, cursoId)
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    // Método para obtener la lista de alumnos de la base de datos
    private fun obtenerAlumnosPorCurso(cursoId: Int) {
        val datAlumno = DatAlumno()
        listaAlumnos = datAlumno.obtenerAlumnosPorCurso(cursoId)  // Llamamos al método de DatAlumno para obtener los alumnos

        // Actualizar la lista en el adaptador
        alumnoAdapter.actualizarAlumnos(listaAlumnos)
    }

    // Método para filtrar los alumnos según el texto de búsqueda
    private fun filtrarAlumnos(query: String, cursoId: Int) {
        val datAlumno = DatAlumno()
        val alumnosFiltrados = datAlumno.buscarAlumnosPorNombreApellidoYCurso(query, cursoId) // Llamamos al método que filtra por nombre, apellido y curso
        alumnoAdapter.actualizarAlumnos(alumnosFiltrados)
    }
}
