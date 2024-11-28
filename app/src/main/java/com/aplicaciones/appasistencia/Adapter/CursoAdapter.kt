package com.aplicaciones.appasistencia.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.AsistenciaProfesor
import com.aplicaciones.appasistencia.DetallesAlumnosCurso
import com.aplicaciones.appasistencia.Entidad.Curso
import com.aplicaciones.appasistencia.MostrarQrCurso
import com.aplicaciones.appasistencia.R

class CursoAdapter(private val cursos: List<Curso>) : RecyclerView.Adapter<CursoAdapter.CursoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_curso, parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        val curso = cursos[position]
        holder.tvNombreCurso.text = curso.nombreCurso
        holder.tvCodigoCurso.text = curso.codigoCurso
        holder.tvHoraCurso.text = "${curso.horaInicio} - ${curso.horaFin}"
        holder.tvDiaCurso.text = curso.diaSemana

        // Configurar el botón "Mostrar QR"
        holder.btnMostrarQR.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MostrarQrCurso::class.java)
            intent.putExtra("ID_Curso", curso.id) // Pasar el ID del curso a la nueva actividad
            context.startActivity(intent)

        }

        // Configurar el botón "Asistencia"
        holder.btnAsistencia.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AsistenciaProfesor::class.java)
            intent.putExtra("ID_Curso", curso.id) // Pasar el ID del curso
            Log.d("Boton asistencia", "ID del curso: ${curso.id}")
            Log.d("Boton asistencia", "ID del profesor: ${curso.profesorId}")
            intent.putExtra("ID_Profesor", curso.profesorId) // Pasar el ID del profesor
            context.startActivity(intent)
        }

        // Configurar el botón "Alumnos"
        holder.btnAlumnos.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetallesAlumnosCurso::class.java)
            intent.putExtra("ID_Curso", curso.id) // Pasar el ID del curso
            intent.putExtra("ID_Profesor", curso.profesorId) // Pasar el ID del profesor
            Log.d("Boton alumnos", "ID del curso: ${curso.id}")
            Log.d("Boton alumnos", "ID del profesor: ${curso.profesorId}")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return cursos.size
    }

    inner class CursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreCurso: TextView = itemView.findViewById(R.id.tvNombreCurso)
        val tvCodigoCurso: TextView = itemView.findViewById(R.id.tvCodigoCurso)
        val tvHoraCurso: TextView = itemView.findViewById(R.id.tvHoraCurso)
        val tvDiaCurso: TextView = itemView.findViewById(R.id.tvDiaCurso)
        val btnMostrarQR: Button = itemView.findViewById(R.id.btnMostrarQR)
        val btnAsistencia: Button = itemView.findViewById(R.id.btnAsistencia)
        val btnAlumnos: Button = itemView.findViewById(R.id.btnAlumnos)
    }
}
