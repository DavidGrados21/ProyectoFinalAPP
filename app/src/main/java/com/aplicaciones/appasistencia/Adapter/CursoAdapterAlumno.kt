package com.aplicaciones.appasistencia.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.DetallesAlumnoAsistencia
import com.aplicaciones.appasistencia.DetallesAsistenciaAlumno
import com.aplicaciones.appasistencia.Entidad.Curso
import com.aplicaciones.appasistencia.R

class CursoAdapterAlumno(public var cursos: List<Curso>, val context: Context) : RecyclerView.Adapter<CursoAdapterAlumno.CursoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_curso_alumno, parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        val curso = cursos[position]
        holder.tvNombreCurso.text = curso.nombreCurso
        holder.tvCodigoCurso.text = curso.codigoCurso
        holder.tvHoraCurso.text = "${curso.horaInicio} - ${curso.horaFin}"
        holder.tvDiaCurso.text = curso.diaSemana
        val sharedPreferences = context.getSharedPreferences("AppAsistencia", Context.MODE_PRIVATE)
        val alumnoId = sharedPreferences.getInt("ID_Alumno", -1)  // Obtiene el ID, -1 es el valor por defecto si no existe

        // Configurar el botón de detalles de asistencia
        holder.btnDetallesAsistencia.setOnClickListener {
            // Pasar el ID del alumno y del curso a la actividad de detalles de asistencia
            val intent = Intent(context, DetallesAsistenciaAlumno::class.java)
            intent.putExtra("ID_Alumno", alumnoId)
            intent.putExtra("ID_Curso", curso.id)  // Pasar el ID del curso
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return cursos.size
    }

    class CursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreCurso: TextView = itemView.findViewById(R.id.tvNombreCurso)
        val tvCodigoCurso: TextView = itemView.findViewById(R.id.tvCodigoCurso)
        val tvHoraCurso: TextView = itemView.findViewById(R.id.tvHoraCurso)
        val tvDiaCurso: TextView = itemView.findViewById(R.id.tvDiaCurso)
        val btnDetallesAsistencia: Button = itemView.findViewById(R.id.btnDetallesAsistencia) // El botón
    }
}
