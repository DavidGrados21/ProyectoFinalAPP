package com.aplicaciones.appasistencia

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aplicaciones.appasistencia.Datos.DatAlumno
import java.io.ByteArrayOutputStream
import java.io.IOException

class EditPerfilAlumno : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageProfile: ImageView
    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etPhone: EditText
    private var alumnoId: Int = -1
    private var imageBase64: String? = null // Variable para almacenar la imagen en Base64

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_perfil_alumno)

        // Inicialización de las vistas
        imageProfile = findViewById(R.id.imageProfile)
        etName = findViewById(R.id.etName)
        etSurname = findViewById(R.id.etSurname)
        etPhone = findViewById(R.id.etPhone)

        // Obtener el ID del alumno pasado desde la actividad anterior
        alumnoId = intent.getIntExtra("ID_Alumno", -1)
        Log.d("EditPerfil", "ID del alumno: $alumnoId")

        if (alumnoId != -1) {
            cargarDatosAlumno(alumnoId)
        } else {
            Toast.makeText(this, "Error al cargar los datos del alumno", Toast.LENGTH_SHORT).show()
        }

        // Cambiar la imagen desde la galería
        val btnChangeImage: Button = findViewById(R.id.btnChangeImage)
        btnChangeImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Guardar los cambios
        val btnSave: Button = findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            guardarCambios()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()

                    // Convertir la imagen a Base64
                    imageBase64 = convertirImagenABase64(bitmap)
                    imageProfile.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cargarDatosAlumno(id: Int) {
        val datAlumno = DatAlumno()
        val alumno = datAlumno.obtenerAlumnoPorId(id)

        alumno?.let {
            etName.setText(it.nombre)
            etSurname.setText(it.apellido)
            etPhone.setText(it.telefono)
            Log.d("EditPerfil", "Imagen de la base de datos: ${it.foto}")

            // Verificar si la imagen de la base de datos es null
            if (it.foto != null) {
                // Cargar imagen desde Base64 si existe
                imageBase64 = it.foto
                val bitmap = convertirBase64ABitmap(it.foto)
                imageProfile.setImageBitmap(bitmap)
            } else {
            imageProfile.setImageResource(R.drawable.ima)

            }
        }
    }




    private fun guardarCambios() {
        val updatedName = etName.text.toString().trim()
        val updatedSurname = etSurname.text.toString().trim()
        val updatedPhone = etPhone.text.toString().trim()

        if (updatedName.isEmpty() || updatedSurname.isEmpty() || updatedPhone.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val datAlumno = DatAlumno()
        val result = datAlumno.actualizarAlumno(alumnoId, updatedName, updatedSurname, updatedPhone, imageBase64)

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()

        if (result == "Alumno actualizado exitosamente") {
            // Devuelve el ID del alumno como resultado
            val intent = Intent()
            intent.putExtra("ID_Alumno", alumnoId)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


    private fun redimensionarImagen(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        // Asegúrate de que las dimensiones sean positivas antes de proceder
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            throw IllegalArgumentException("Las dimensiones de la imagen no pueden ser cero o negativas")
        }

        val ratio = Math.min(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height)
        val width = (bitmap.width * ratio).toInt()
        val height = (bitmap.height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    private fun convertirImagenABase64(bitmap: Bitmap): String {
        // Redimensiona la imagen a un tamaño máximo de 800x800
        val resizedBitmap = redimensionarImagen(bitmap, 800, 800)

        // Usa ByteArrayOutputStream para convertir la imagen redimensionada a Base64
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // Comprime con calidad 80%
        val byteArray = outputStream.toByteArray()

        // Devuelve la imagen en formato Base64
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    private fun convertirBase64ABitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }
}
