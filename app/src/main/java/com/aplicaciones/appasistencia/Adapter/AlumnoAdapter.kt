package com.aplicaciones.appasistencia

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.aplicaciones.appasistencia.Entidad.Alumno

class AlumnoAdapter(private var alumnos: List<Alumno>) : RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder>() {

    // Método para actualizar la lista de alumnos
    fun actualizarAlumnos(nuevaLista: List<Alumno>) {
        alumnos = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alumno, parent, false)
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = alumnos[position]
        holder.tvNombreAlumno.text = alumno.nombre
        holder.tvApellidoAlumno.text = alumno.apellido

        // Convertir la cadena Base64 en una imagen y cargarla en el ImageView
        if (alumno.foto != null) {
            val imagenBitmap = decodeBase64(alumno.foto)
            holder.ivFotoAlumno.setImageBitmap(imagenBitmap)
        }
    }

    override fun getItemCount(): Int = alumnos.size

    // Función para decodificar la imagen Base64
    private fun decodeBase64(fotoBase64: String): Bitmap {
        val decodedString = Base64.decode(fotoBase64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    inner class AlumnoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreAlumno: TextView = view.findViewById(R.id.tvNombreAlumno)
        val tvApellidoAlumno: TextView = view.findViewById(R.id.tvApellidoAlumno)
        val ivFotoAlumno: ImageView = view.findViewById(R.id.ivFotoAlumno)
    }
}
