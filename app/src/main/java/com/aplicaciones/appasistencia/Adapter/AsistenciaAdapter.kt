package com.aplicaciones.appasistencia.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aplicaciones.appasistencia.Datos.DatAsistencia
import com.aplicaciones.appasistencia.DetallesAlumnoAsistencia
import com.aplicaciones.appasistencia.QrAsistencia
import com.aplicaciones.appasistencia.R

class AsistenciaAdapter(private val listaAsistencias: List<Triple<Int, String, Boolean>>) :
    RecyclerView.Adapter<AsistenciaAdapter.AsistenciaViewHolder>() {

    class AsistenciaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIdAsistencia: TextView = view.findViewById(R.id.tvIdAsistencia)
        val tvFechaAsistencia: TextView = view.findViewById(R.id.tvFechaAsistencia)
        val switchPermitirAsistencia: Switch = view.findViewById(R.id.switchPermitirAsistencia)
        val btnGenerarQR: Button = view.findViewById(R.id.btnGenerarQR)
        val btnAsistenciaAlumnos: Button = view.findViewById(R.id.btnAsistenciaAlumnos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsistenciaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_asistencia, parent, false)
        return AsistenciaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AsistenciaViewHolder, position: Int) {
        val (idAsistencia, fecha, permitirAsistencia) = listaAsistencias[position]
        holder.tvIdAsistencia.text = "ID: $idAsistencia"
        holder.tvFechaAsistencia.text = "Fecha: $fecha"
        holder.switchPermitirAsistencia.isChecked = permitirAsistencia

        // Listener para el Switch
        holder.switchPermitirAsistencia.setOnCheckedChangeListener { _, isChecked ->
            val context = holder.itemView.context
            try {
                // Llamamos al método de DatAsistencia para actualizar el estado
                val datAsistencia = DatAsistencia()
                val resultMessage = datAsistencia.updatePermitirAsistencia(idAsistencia, isChecked)

                Toast.makeText(context, resultMessage, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("AsistenciaAdapter", "Error al actualizar el estado: ${e.message}")
                Toast.makeText(context, "Error al actualizar el estado", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar botón "Generar QR"
        holder.btnGenerarQR.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, QrAsistencia::class.java)
            intent.putExtra("ID_Asistencia", idAsistencia)
            context.startActivity(intent)
        }

        // Configurar botón "Asistencia Alumnos"
        holder.btnAsistenciaAlumnos.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetallesAlumnoAsistencia::class.java)
            intent.putExtra("ID_Asistencia", idAsistencia)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaAsistencias.size
    }
}
