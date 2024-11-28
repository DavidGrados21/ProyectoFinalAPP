package com.aplicaciones.appasistencia

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aplicaciones.appasistencia.Datos.DatCurso
import com.aplicaciones.appasistencia.Entidad.Curso


class Registrar_Curso : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_curso)

        // Obtener el ID del profesor desde el Intent
        val profesorId = intent.getIntExtra("ID_Profesor", -1)

        // Referencias a los campos de entrada
        val etNombreCurso: EditText = findViewById(R.id.etNombreCurso)
        val etDescripcion: EditText = findViewById(R.id.etDescripcion)
        val etCodigoCurso: EditText = findViewById(R.id.etCodigoCurso)
        val timePickerInicio: TimePicker = findViewById(R.id.timePickerInicio)
        val timePickerFin: TimePicker = findViewById(R.id.timePickerFin)
        val etDiaSemana: EditText = findViewById(R.id.etDiaSemana)

        val btnRegistrarCurso: Button = findViewById(R.id.btnRegistrarCurso)

        // Acción del botón para registrar el curso
        btnRegistrarCurso.setOnClickListener {
            val nombreCurso = etNombreCurso.text.toString()
            val descripcion = etDescripcion.text.toString()
            val codigoCurso = etCodigoCurso.text.toString()
            val diaSemana = etDiaSemana.text.toString()

            // Obtener la hora y minuto seleccionados de los TimePicker
            // Obtener la hora y minuto seleccionados de los TimePicker
            val horaInicio = "${timePickerInicio.currentHour}:${timePickerInicio.currentMinute}"
            val horaFin = "${timePickerFin.currentHour}:${timePickerFin.currentMinute}"


            // Validar que los campos no estén vacíos
            if (nombreCurso.isEmpty() || descripcion.isEmpty() || codigoCurso.isEmpty() ||
                horaInicio.isEmpty() || horaFin.isEmpty() || diaSemana.isEmpty()) {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
            } else {
                val curso = Curso(
                    id = 0,
                    nombreCurso = nombreCurso,
                    descripcion = descripcion,
                    codigoCurso = codigoCurso,
                    profesorId = profesorId,
                    horaInicio = horaInicio,
                    horaFin = horaFin,
                    diaSemana = diaSemana
                )

                val resultado = DatCurso().insertarCurso(curso)
                if (resultado == "Curso registrado exitosamente") {
                    Toast.makeText(this, "Curso registrado exitosamente", Toast.LENGTH_SHORT).show()
                    finish() // Cerrar la actividad y regresar
                } else {
                    Toast.makeText(this, "Error al registrar el curso", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
