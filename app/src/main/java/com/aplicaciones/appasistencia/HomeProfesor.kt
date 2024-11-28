package com.aplicaciones.appasistencia

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.aplicaciones.appasistencia.Datos.DatProfesor
import java.text.SimpleDateFormat
import java.util.*

class HomeProfesor : AppCompatActivity() {
    private lateinit var tvDateTime: TextView
    private lateinit var tvTime: TextView  // TextView para la hora
    private val handler = android.os.Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_profesor)

        // Inicializar los TextViews
        tvDateTime = findViewById(R.id.tvDate)
        tvTime = findViewById(R.id.tvTime)

        // Actualización de la fecha y hora
        updateDateTime()

        // Obtener el ID del profesor pasado desde la actividad anterior
        val profesorId = intent.getIntExtra("ID_USUARIO", -1)
        Log.d("HomeProfesor", "ID del profesor: $profesorId")

        // Verificar si el ID es válido
        if (profesorId != -1) {
            // Cargar datos del profesor
            cargarDatosProfesor(profesorId)
        } else {
            Toast.makeText(this, "Error al cargar los datos del profesor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDateTime() {
        // Define el formato deseado para la fecha
        val dateFormat = SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy", Locale.getDefault())
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

    private fun cargarDatosProfesor(id: Int) {
        Log.d("HomeProfesor", "Cargando datos del profesor con ID: $id")
        val datProfesor = DatProfesor()
        val profesor = datProfesor.obtenerProfesorPorId(id)
        Log.d("HomeProfesor", "Profesor obtenido: $profesor")

        if (profesor != null) {
            // Actualizar los campos del diseño
            val tvName: TextView = findViewById(R.id.tvName)
            val tvEmail: TextView = findViewById(R.id.tvEmail)
            val tvPhone: TextView = findViewById(R.id.tvPhone)
            val imageProfile: ImageView = findViewById(R.id.imageProfile)

            tvName.text = "${profesor.nombre} ${profesor.apellido}"
            tvEmail.text = profesor.correo
            tvPhone.text = "Tel: ${profesor.telefono}"

            // Cargar la imagen desde Base64
            if (!profesor.foto.isNullOrEmpty()) {
                try {
                    val bitmap = convertirBase64ABitmap(profesor.foto)
                    if (bitmap != null) {
                        imageProfile.setImageBitmap(bitmap)
                    } else {
                        // Cargar imagen predeterminada si la conversión falla
                        imageProfile.setImageResource(R.drawable.ima)
                    }
                } catch (e: Exception) {
                    Log.e("HomeProfesor", "Error al convertir Base64 a Bitmap: ${e.message}")
                    imageProfile.setImageResource(R.drawable.ima)
                }
            } else {
                // Cargar imagen predeterminada si no hay imagen
                imageProfile.setImageResource(R.drawable.ima)
            }
        } else {
            Toast.makeText(this, "No se encontró al profesor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertirBase64ABitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            Log.e("HomeProfesor", "Error al decodificar Base64: ${e.message}")
            null
        }
    }


    // Método para editar el perfil del profesor
    fun openEditProfileActivity(view: View) {
        // Obtener el ID del profesor nuevamente si es necesario
        val profesorId = intent.getIntExtra("ID_USUARIO", -1)

        if (profesorId != -1) {
            val intent = Intent(this, EditPerfilProfesor::class.java)
            intent.putExtra("ID_Profesor", profesorId)  // Pasar el ID del profesor
            Log.d("HomeProfesor", "ID del profesor enviado a EditPerfilProfesor: $profesorId")
            startActivityForResult(intent, 1001)  // Código de solicitud
        } else {
            Toast.makeText(this, "Error al obtener el ID del profesor", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            val profesorId = data.getIntExtra("ID_Profesor", -1)
            if (profesorId != -1) {
                Log.d("HomeProfesor", "Actualizando datos del profesor con ID: $profesorId")
                cargarDatosProfesor(profesorId) // Recargar los datos del profesor actualizado
            } else {
                Log.e("HomeProfesor", "No se recibió un ID válido del profesor.")
            }
        }
    }

    fun cerrarSesion(view: View) {
        // Lógica para cerrar sesión
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual
    }
    fun openCursoProfesor(view: View) {
        // Obtener el ID del profesor nuevamente si es necesario
        val profesorId = intent.getIntExtra("ID_USUARIO", -1)

        if (profesorId != -1) {
            val intent = Intent(this, CursoPorfesor::class.java)
            intent.putExtra("ID_Profesor", profesorId)  // Pasar el ID del profesor
            Log.d("HomeProfesor", "ID del profesor enviado a EditPerfilProfesor: $profesorId")
            startActivityForResult(intent, 1001)  // Código de solicitud
        } else {
            Toast.makeText(this, "Error al obtener el ID del profesor", Toast.LENGTH_SHORT).show()
        }
    }
}
