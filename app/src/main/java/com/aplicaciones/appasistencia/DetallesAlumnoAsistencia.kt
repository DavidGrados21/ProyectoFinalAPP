package com.aplicaciones.appasistencia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.Adapter.DetallesAsistenciaAdapter
import com.aplicaciones.appasistencia.Datos.DatAsistencia

class DetallesAlumnoAsistencia : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_alumno_asistencia)

        val rvListaAlumnos: RecyclerView = findViewById(R.id.rvListaAlumnos)
        val idAsistencia = intent.getIntExtra("ID_Asistencia", -1)
        // Obtener el ID de asistencia (puedes pasarlo a través de un intent o un argumento)
        val asistenciaId = idAsistencia  // Cambia esto según cómo obtienes el ID de la asistencia

        // Obtener la lista de alumnos de la base de datos
        val datAsistencia = DatAsistencia()
        val listaAlumnos = datAsistencia.listarAlumnosPorAsistencia(asistenciaId)

        // Crear el adaptador con la lista de alumnos
        val adapter = DetallesAsistenciaAdapter(listaAlumnos)

        // Configurar el RecyclerView
        rvListaAlumnos.layoutManager = LinearLayoutManager(this)
        rvListaAlumnos.adapter = adapter
    }
}
