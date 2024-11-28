package com.aplicaciones.appasistencia.Datos

import android.util.Log
import com.aplicaciones.appasistencia.DBConnection
import com.aplicaciones.appasistencia.Entidad.Alumno
import com.aplicaciones.appasistencia.Entidad.Profesor
import java.sql.SQLException
import java.sql.Statement

class DatProfesor {
    // Método para insertar un alumno
    fun insertarProfesor(profesor: Profesor): String {
        val conn = DBConnection.getConnection() // Usar la conexión de DBConnection
        if (conn != null) {
            try {
                val sql = "INSERT INTO Profesores (Nombre, Apellido, Correo, Contraseña, Telefono, Foto) VALUES ('${profesor.nombre}', '${profesor.apellido}', '${profesor.correo}', '${profesor.contraseña}', '${profesor.telefono}', '${profesor.foto}')"
                val statement: Statement = conn.createStatement()
                val rowsAffected = statement.executeUpdate(sql)

                if (rowsAffected > 0) {
                    return "Profesor registrado exitosamente"
                } else {
                    return "Error al registrar al Profesor"
                }
            } catch (ex: SQLException) {
                // Verificar si el error es de violación de clave única en SQL Server
                if (ex.sqlState == "23000" || ex.errorCode == 2627 || ex.errorCode == 2601) {
                    return "El correo ya está registrado, porfavor intente con otro"
                } else {
                    // Otro tipo de error
                    return "Error al registrar al Profesor: ${ex.message}"
                }
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
        return "Error de conexión"
    }
    fun obtenerProfesorPorId(id: Int): Profesor? {
        val conn = DBConnection.getConnection() // Usar la conexión de DBConnection
        if (conn != null) {
            try {
                val sql = "SELECT * FROM Profesores WHERE ProfesorID = $id"
                val statement = conn.createStatement()
                val resultSet = statement.executeQuery(sql)

                if (resultSet.next()) {
                    // Crear un objeto Profesor con los datos obtenidos de la base de datos
                    return Profesor(
                        id = resultSet.getInt("ProfesorID"),
                        nombre = resultSet.getString("Nombre"),
                        apellido = resultSet.getString("Apellido"),
                        correo = resultSet.getString("Correo"),
                        contraseña = resultSet.getString("Contraseña"),
                        telefono = resultSet.getString("Telefono"),
                        foto = resultSet.getString("Foto"),
                        fechaRegistro = resultSet.getString("FechaRegistro")
                    )
                }
            } catch (ex: SQLException) {
                ex.printStackTrace()
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
        return null // Si no se encuentra el profesor o hay un error
    }
    fun actualizarProfesor(id: Int, nombre: String, apellido: String, telefono: String, imagen: String? = null): String {
        val conn = DBConnection.getConnection() // Obtener conexión desde DBConnection
        if (conn != null) {
            try {
                // Construcción dinámica de la consulta SQL
                val sql = if (imagen != null) {
                    """
                UPDATE Profesores 
                SET Nombre = ?, Apellido = ?, Telefono = ?, Foto = ? 
                WHERE ProfesorID = ?
                """
                } else {
                    """
                UPDATE Profesores 
                SET Nombre = ?, Apellido = ?, Telefono = ? 
                WHERE ProfesorID = ?
                """
                }

                // Preparar la sentencia SQL
                val preparedStatement = conn.prepareStatement(sql)

                // Configuración de los parámetros
                preparedStatement.setString(1, nombre)
                preparedStatement.setString(2, apellido)
                preparedStatement.setString(3, telefono)
                if (imagen != null) {
                    preparedStatement.setString(4, imagen)
                    preparedStatement.setInt(5, id)
                } else {
                    preparedStatement.setInt(4, id)
                }

                // Ejecutar la consulta
                val rowsAffected = preparedStatement.executeUpdate()
                return if (rowsAffected > 0) {
                    "Profesor actualizado exitosamente"
                } else {
                    "No se encontraron datos para actualizar"
                }
            } catch (ex: SQLException) {
                Log.e("DatProfesor", "Error SQL: ${ex.message}")
                ex.printStackTrace()
                return "Error al actualizar al profesor: ${ex.message}"
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatProfesor", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        return "Error de conexión"
    }

}