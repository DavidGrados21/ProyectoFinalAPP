package com.aplicaciones.appasistencia.Datos

import android.util.Log
import com.aplicaciones.appasistencia.DBConnection
import com.aplicaciones.appasistencia.Entidad.AlumnoPorCurso
import java.sql.SQLException
import java.sql.Statement

class DatAlumnoCurso {

    // Método para registrar la inscripción de un alumno en un curso
    fun registrarAlumnoCurso(alumnoID: Int, cursoID: Int): String {
        val conn = DBConnection.getConnection() // Usar la conexión desde DBConnection
        if (conn != null) {
            try {
                // SQL para insertar un registro de AlumnoPorCurso
                val sql = """
                    INSERT INTO AlumnoPorCurso (AlumnoID, CursoID)
                    VALUES ('$alumnoID', '$cursoID')
                """
                val statement: Statement = conn.createStatement()
                val rowsAffected = statement.executeUpdate(sql)

                if (rowsAffected > 0) {
                    return "Alumno inscrito exitosamente en el curso"
                } else {
                    return "Error al registrar la inscripción del alumno"
                }
            } catch (ex: SQLException) {
                // Manejo de errores SQL
                if (ex.sqlState == "23000" || ex.errorCode == 2627 || ex.errorCode == 2601) {
                    return "Error de clave única, el alumno ya está inscrito en este curso"
                } else {
                    return "Error al registrar la inscripción: ${ex.message}"
                }
            } finally {
                try {
                    conn.close() // Cerrar la conexión
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
        return "Error de conexión a la base de datos"
    }

    // Método para obtener los alumnos inscritos en un curso específico
    fun obtenerAlumnosPorCurso(cursoID: Int): List<AlumnoPorCurso> {
        val conn = DBConnection.getConnection()
        val alumnosPorCurso = mutableListOf<AlumnoPorCurso>()
        if (conn != null) {
            try {
                // SQL para obtener todos los alumnos inscritos en un curso
                val sql = "SELECT * FROM AlumnoPorCurso WHERE CursoID = $cursoID"
                val statement = conn.createStatement()
                val resultSet = statement.executeQuery(sql)

                // Recorrer los resultados y agregar a la lista
                while (resultSet.next()) {
                    val alumnoPorCurso = AlumnoPorCurso(
                        alumnoPorCursoID = resultSet.getInt("AlumnoPorCursoID"),
                        alumnoID = resultSet.getInt("AlumnoID"),
                        cursoID = resultSet.getInt("CursoID"),
                        fechaInscripcion = resultSet.getString("FechaInscripcion")
                    )
                    alumnosPorCurso.add(alumnoPorCurso)
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
        return alumnosPorCurso
    }
}
