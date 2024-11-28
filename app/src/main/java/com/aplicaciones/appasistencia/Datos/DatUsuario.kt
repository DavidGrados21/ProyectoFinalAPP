package com.aplicaciones.appasistencia.Datos

import com.aplicaciones.appasistencia.DBConnection
import com.aplicaciones.appasistencia.Entidad.Usuario
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class DatUsuario {

    fun iniciarSesion(correo: String, contraseña: String): Usuario? {
        val conn: Connection? = DBConnection.getConnection()

        if (conn != null) {
            try {
                // Consultar en la tabla Profesores
                val sqlProfesor = """
                    SELECT ProfesorID AS ID, Nombre, Apellido, Correo, Telefono, Foto, 'Profesor' AS Rol
                    FROM Profesores
                    WHERE Correo = ? AND Contraseña = ?
                """
                val statementProfesor = conn.prepareStatement(sqlProfesor)
                statementProfesor.setString(1, correo)
                statementProfesor.setString(2, contraseña)
                val rsProfesor: ResultSet = statementProfesor.executeQuery()

                if (rsProfesor.next()) {
                    return Usuario(
                        id = rsProfesor.getInt("ID"),
                        nombre = rsProfesor.getString("Nombre"),
                        apellido = rsProfesor.getString("Apellido"),
                        correo = rsProfesor.getString("Correo"),
                        telefono = rsProfesor.getString("Telefono"),
                        foto = rsProfesor.getString("Foto"),
                        rol = rsProfesor.getString("Rol")
                    )
                }

                // Consultar en la tabla Alumnos
                val sqlAlumno = """
                    SELECT AlumnoID AS ID, Nombre, Apellido, Correo, Telefono, Foto, 'Alumno' AS Rol
                    FROM Alumnos
                    WHERE Correo = ? AND Contraseña = ?
                """
                val statementAlumno = conn.prepareStatement(sqlAlumno)
                statementAlumno.setString(1, correo)
                statementAlumno.setString(2, contraseña)
                val rsAlumno: ResultSet = statementAlumno.executeQuery()

                if (rsAlumno.next()) {
                    return Usuario(
                        id = rsAlumno.getInt("ID"),
                        nombre = rsAlumno.getString("Nombre"),
                        apellido = rsAlumno.getString("Apellido"),
                        correo = rsAlumno.getString("Correo"),
                        telefono = rsAlumno.getString("Telefono"),
                        foto = rsAlumno.getString("Foto"),
                        rol = rsAlumno.getString("Rol")
                    )
                }

                return null
            } catch (ex: SQLException) {
                ex.printStackTrace()
                return null
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }
}
