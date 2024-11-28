package com.aplicaciones.appasistencia.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.AlumnoAsistencia
import com.aplicaciones.appasistencia.R

// Adaptador para listar los alumnos con su asistencia
class DetallesAsistenciaAdapter(private val listaAlumnos: List<AlumnoAsistencia>) :
    RecyclerView.Adapter<DetallesAsistenciaAdapter.AlumnoViewHolder>() {

    // ViewHolder para contener las vistas de cada item
    class AlumnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreAlumno)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstadoAsistencia)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaAsistencia)
    }

    // Crear la vista para cada item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alumno_asistencia, parent, false)
        return AlumnoViewHolder(view)
    }

    // Asignar datos al item en la posición correspondiente
    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = listaAlumnos[position]
        holder.tvNombre.text = alumno.nombre
        holder.tvEstado.text = "Asistencia: ${if (alumno.presente) "Presente" else "Ausente"}"
        holder.tvFecha.text = "Fecha: ${alumno.fecha}"
    }

    // Devolver el tamaño de la lista
    override fun getItemCount(): Int {
        return listaAlumnos.size
    }
}
