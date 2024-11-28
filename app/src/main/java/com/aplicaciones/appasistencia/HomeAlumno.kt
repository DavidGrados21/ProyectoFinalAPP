package com.aplicaciones.appasistencia

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.util.Base64
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aplicaciones.appasistencia.Datos.DatAlumno
import java.util.Locale
import android.os.Handler

class HomeAlumno : AppCompatActivity() {
    private lateinit var tvDateTime: TextView
    private lateinit var tvTime: TextView  // TextView para la hora
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_alumno)
        // Inicializar los TextViews
        tvDateTime = findViewById(R.id.tvDate)
        tvTime = findViewById(R.id.tvTime)

        // Actualización de la fecha y hora
        updateDateTime()
        // Obtener el ID del alumno pasado desde la actividad anterior
        val alumnoId = intent.getIntExtra("ID_USUARIO", -1)
        Log.d("HomeAlumno", "ID del alumno: $alumnoId")
        // Verificar si el ID es válido
        if (alumnoId != -1) {
            // Cargar datos del alumno
            cargarDatosAlumno(alumnoId)
        } else {
            Toast.makeText(this, "Error al cargar los datos del alumno", Toast.LENGTH_SHORT).show()
        }

    }
    private fun updateDateTime() {
        // Define el formato deseado
        val dateFormat = SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy", Locale.getDefault())

        // Actualiza el TextView con la fecha actual en el formato deseado
        val currentDate = dateFormat.format(Date())
        tvDateTime.text = currentDate
        // Define el formato para la hora
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime = timeFormat.format(Date())
        tvTime.text = currentTime  // Establece la hora en el TextView correspondiente

        // Repite la actualización cada segundo (1000 milisegundos)
        handler.postDelayed({
            updateDateTime()
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)  // Detiene las actualizaciones cuando se destruye la actividad
    }
    private fun cargarDatosAlumno(id: Int) {
        Log.d("HomeAlumno", "Cargando datos del alumno con ID: $id")
        val datAlumno = DatAlumno()
        val alumno = datAlumno.obtenerAlumnoPorId(id)
        Log.d("HomeAlumno", "Alumno obtenido: $alumno")

        if (alumno != null) {
            // Actualizar los campos del diseño
            val tvName: TextView = findViewById(R.id.tvName)
            val tvEmail: TextView = findViewById(R.id.tvEmail)
            val tvPhone: TextView = findViewById(R.id.tvPhone)
            val imageProfile: ImageView = findViewById(R.id.imageProfile)

            intent.putExtra("ID_Alumno", alumno.id)  // Pasar el ID del alumno
            Log.d("HomeAlumno", "ID del alumno enviado: ${alumno.id}")

            tvName.text = "${alumno.nombre} ${alumno.apellido}"
            tvEmail.text = alumno.correo
            tvPhone.text = "Tel: ${alumno.telefono}"

            // Cargar la imagen desde Base64
            if (!alumno.foto.isNullOrEmpty()) {
                try {
                    val bitmap = convertirBase64ABitmap(alumno.foto)
                    if (bitmap != null) {
                        imageProfile.setImageBitmap(bitmap)
                    } else {
                        // Cargar imagen predeterminada si la conversión falla
                        imageProfile.setImageResource(R.drawable.ima)
                    }
                } catch (e: Exception) {
                    Log.e("HomeAlumno", "Error al convertir Base64 a Bitmap: ${e.message}")
                    imageProfile.setImageResource(R.drawable.ima)
                }
            } else {
                // Cargar imagen predeterminada si no hay imagen
                imageProfile.setImageResource(R.drawable.ima)
            }
        } else {
            Toast.makeText(this, "No se encontró al alumno", Toast.LENGTH_SHORT).show()
        }
    }
    fun openEditProfileActivity(view: View) {
        // Obtener el ID del alumno nuevamente si es necesario
        val alumnoId = intent.getIntExtra("ID_USUARIO", -1)

        if (alumnoId != -1) {
            val intent = Intent(this, EditPerfilAlumno::class.java)
            intent.putExtra("ID_Alumno", alumnoId)  // Pasar el ID del alumno
            Log.d("HomeAlumno", "ID del alumno enviado a EditPerfilAlumno: $alumnoId")
            startActivityForResult(intent, 1001)  // Código de solicitud
        } else {
            Toast.makeText(this, "Error al obtener el ID del alumno", Toast.LENGTH_SHORT).show()
        }
    }
    private fun convertirBase64ABitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            Log.e("HomeAlumno", "Error al decodificar Base64: ${e.message}")
            null
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            val alumnoId = data.getIntExtra("ID_Alumno", -1)
            if (alumnoId != -1) {
                Log.d("HomeAlumno", "Actualizando datos del alumno con ID: $alumnoId")
                cargarDatosAlumno(alumnoId) // Recargar los datos del alumno actualizado
            } else {
                Log.e("HomeAlumno", "No se recibió un ID válido del alumno.")
            }
        }
    }
    fun openCursoAlumno(view: View) {
        // Obtener el ID del profesor nuevamente si es necesario
        val Alumno = intent.getIntExtra("ID_USUARIO", -1)

        if (Alumno != -1) {
            val intent = Intent(this, CursoAlumno::class.java)
            intent.putExtra("ID_AlumnoId", Alumno)  // Pasar el ID del alumno
            Log.d("HomeAlumno", "ID del Alumno: $Alumno")
            startActivityForResult(intent, 1001)  // Código de solicitud
        } else {
            Toast.makeText(this, "Error al obtener el ID del Alumno", Toast.LENGTH_SHORT).show()
        }
    }
    fun openQrAsistencia(view: View) {
        val Alumno = intent.getIntExtra("ID_USUARIO", -1)

        // Crear el Intent para abrir la actividad CursoAlumnoActivity
        intent.putExtra("ID_AlumnoId", Alumno)  // Pasar el ID del alumno

        val intent = Intent(this, AsistenciaQrEdit::class.java)
        intent.putExtra("ID_AlumnoId", Alumno)  // Pasar el ID del alumno

        startActivity(intent)
    }

    fun cerrarSesion(view: View) {
        // Lógica para cerrar sesión
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual
    }

}