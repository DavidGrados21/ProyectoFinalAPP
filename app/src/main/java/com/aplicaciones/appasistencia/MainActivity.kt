package com.aplicaciones.appasistencia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aplicaciones.appasistencia.Datos.DatUsuario

class MainActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegisterNow: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa los componentes de la interfaz de usuario
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegisterNow = findViewById(R.id.tvRegisterNow)

        // Manejar el click en el botón de iniciar sesión
        btnLogin.setOnClickListener {
            val correo = etEmail.text.toString()
            val contraseña = etPassword.text.toString()

            // Aquí validas el correo y la contraseña, luego de eso puedes hacer el login
            if (correo.isNotEmpty() && contraseña.isNotEmpty()) {
                // Llamar al método de login con los parámetros de correo y contraseña
                iniciarSesion(correo, contraseña)
            } else {
                // Mostrar un mensaje de error si alguno de los campos está vacío
                Toast.makeText(this, "Por favor, ingresa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Al hacer clic en "¿Aún no tienes una cuenta? Regístrate aquí"
        tvRegisterNow.setOnClickListener {
            val intent = Intent(this, MenuRigistro::class.java)
            startActivity(intent)
        }
    }

    // Método para verificar las credenciales y manejar el login
    private fun iniciarSesion(correo: String, contraseña: String) {
        // Aquí llamarías a tu clase `DatUsuario` para verificar las credenciales
        val datUsuario = DatUsuario()
        val usuario = datUsuario.iniciarSesion(correo, contraseña)

        if (usuario != null) {
            // Determinar actividad según el rol del usuario
            val intent = when (usuario.rol) {
                "Alumno" -> Intent(this, HomeAlumno::class.java) // Actividad para administradores
                "Profesor" -> Intent(this, HomeProfesor::class.java) // Actividad para usuarios normales
                else -> {
                    Toast.makeText(this, "Rol desconocido", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            // Pasar información adicional si es necesario (como ID)
            intent.putExtra("ID_USUARIO", usuario.id)

            // Iniciar la actividad y cerrar la pantalla de login
            startActivity(intent)
            finish()
        } else {
            // Si las credenciales son incorrectas, muestra un mensaje
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

}
