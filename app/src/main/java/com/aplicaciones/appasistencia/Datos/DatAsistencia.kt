package com.aplicaciones.appasistencia.Datos

import android.util.Log
import com.aplicaciones.appasistencia.AlumnoAsistencia
import com.aplicaciones.appasistencia.DBConnection
import java.sql.CallableStatement
import java.sql.SQLException

class DatAsistencia {

    // Método para actualizar la asistencia
    fun actualizarAsistencia(alumnoId: Int, asistenciaId: Int): String {
        val conn = DBConnection.getConnection()
        if (conn != null) {
            try {
                val sql = "{CALL ActualizarAsistencia(?, ?)}"
                val callableStatement: CallableStatement = conn.prepareCall(sql)
                callableStatement.setInt(1, alumnoId)
                callableStatement.setInt(2, asistenciaId)

                callableStatement.execute()
                return "Asistencia actualizada exitosamente"
            } catch (ex: SQLException) {
                Log.e("DatAsistencia", "Error SQL: ${ex.message}")
                ex.printStackTrace()
                return "Error al actualizar la asistencia: ${ex.message}"
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatAsistencia", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        return "Error de conexión"
    }

    // Método para registrar la asistencia por curso
    fun registrarAsistenciaPorCurso(cursoId: Int): String {
        val conn = DBConnection.getConnection()
        if (conn != null) {
            try {
                val sql = "{CALL RegistrarAsistenciaPorCurso(?)}"
                val callableStatement: CallableStatement = conn.prepareCall(sql)
                callableStatement.setInt(1, cursoId)

                callableStatement.execute()
                return "Asistencia registrada exitosamente"
            } catch (ex: SQLException) {
                Log.e("DatAsistencia", "Error SQL: ${ex.message}")
                ex.printStackTrace()
                return "Error al registrar la asistencia: ${ex.message}"
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatAsistencia", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        return "Error de conexión"
    }
    fun listarAsistenciasPorCurso(cursoId: Int): List<Triple<Int, String, Boolean>> {
        val listaAsistencias = mutableListOf<Triple<Int, String, Boolean>>()
        val conn = DBConnection.getConnection()
        if (conn != null) {
            try {
                val sql = "SELECT [AsistenciaID], fecha, PermitirAsistencia FROM Asistencias WHERE [CursoID] = ?"
                val preparedStatement = conn.prepareStatement(sql)
                preparedStatement.setInt(1, cursoId)

                val resultSet = preparedStatement.executeQuery()
                while (resultSet.next()) {
                    val idAsistencia = resultSet.getInt("AsistenciaID")
                    val fecha = resultSet.getString("fecha")
                    val permitido = resultSet.getBoolean("PermitirAsistencia")
                    listaAsistencias.add(Triple(idAsistencia, fecha, permitido))
                }
            } catch (ex: SQLException) {
                Log.e("DatAsistencia", "Error SQL: ${ex.message}")
                ex.printStackTrace()
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatAsistencia", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        } else {
            Log.e("DatAsistencia", "Error de conexión")
        }
        return listaAsistencias
    }

    fun listarAlumnosPorAsistencia(asistenciaId: Int): List<AlumnoAsistencia> {
        val listaAlumnos = mutableListOf<AlumnoAsistencia>()
        val conn = DBConnection.getConnection()
        if (conn != null) {
            try {
                val sql = """
            SELECT 
                A.AlumnoID,
                A.Nombre,
                A.Apellido,
                AD.Presente,
                ASIS.Fecha AS FechaAsistencia
            FROM 
                Alumnos A
            INNER JOIN 
                AsistenciaDetalle AD ON A.AlumnoID = AD.AlumnoID
            INNER JOIN 
                Asistencias ASIS ON AD.AsistenciaID = ASIS.AsistenciaID
            WHERE 
                ASIS.AsistenciaID = ?
            ORDER BY 
                ASIS.Fecha DESC
            """
                val preparedStatement = conn.prepareStatement(sql)
                preparedStatement.setInt(1, asistenciaId)

                val resultSet = preparedStatement.executeQuery()
                while (resultSet.next()) {
                    val alumno = AlumnoAsistencia(
                        nombre = resultSet.getString("Nombre"),
                        presente = resultSet.getBoolean("Presente"),
                        fecha = resultSet.getString("FechaAsistencia")
                    )
                    listaAlumnos.add(alumno)
                }
            } catch (ex: SQLException) {
                Log.e("DatAsistencia", "Error SQL: ${ex.message}")
                ex.printStackTrace()
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatAsistencia", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        } else {
            Log.e("DatAsistencia", "Error de conexión")
        }
        return listaAlumnos
    }
    fun updatePermitirAsistencia(asistenciaId: Int, permitir: Boolean): String {
        val conn = DBConnection.getConnection()
        if (conn != null) {
            try {
                // Consulta SQL para actualizar el valor "PermitirAsistencia"
                val sql = "UPDATE Asistencias SET PermitirAsistencia = ? WHERE AsistenciaID = ?"
                val preparedStatement = conn.prepareStatement(sql)

                // Establecer los parámetros
                preparedStatement.setBoolean(1, permitir)
                preparedStatement.setInt(2, asistenciaId)

                // Ejecutar la actualización
                val rowsAffected = preparedStatement.executeUpdate()

                return if (rowsAffected > 0) {
                    "Permiso de asistencia actualizado correctamente"
                } else {
                    "No se encontró la asistencia con el ID proporcionado"
                }
            } catch (ex: SQLException) {
                Log.e("DatAsistencia", "Error SQL: ${ex.message}")
                ex.printStackTrace()
                return "Error al actualizar el permiso de asistencia: ${ex.message}"
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatAsistencia", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        return "Error de conexión"
    }
    fun obtenerAsistenciaAlumno(alumnoId: Int, cursoId: Int): List<AlumnoAsistencia> {
        val listaAsistencias = mutableListOf<AlumnoAsistencia>()
        val conn = DBConnection.getConnection() // Clase de conexión a la base de datos
        if (conn != null) {
            try {
                val sql = """
                SELECT 
                    A.Nombre AS NombreAlumno,
                    AD.Presente,
                    ASIS.Fecha
                FROM 
                    Alumnos A
                JOIN 
                    AlumnoPorCurso APC ON A.AlumnoID = APC.AlumnoID
                JOIN 
                    Cursos C ON APC.CursoID = C.CursoID
                JOIN 
                    Asistencias ASIS ON C.CursoID = ASIS.CursoID
                JOIN 
                    AsistenciaDetalle AD ON ASIS.AsistenciaID = AD.AsistenciaID AND AD.AlumnoID = A.AlumnoID
                WHERE 
                    A.AlumnoID = ? AND C.CursoID = ?
                ORDER BY 
                    ASIS.Fecha
            """
                val preparedStatement = conn.prepareStatement(sql)
                preparedStatement.setInt(1, alumnoId)
                preparedStatement.setInt(2, cursoId)

                val resultSet = preparedStatement.executeQuery()
                while (resultSet.next()) {
                    val asistencia = AlumnoAsistencia(
                        nombre = resultSet.getString("NombreAlumno"),
                        presente = resultSet.getBoolean("Presente"),
                        fecha = resultSet.getString("Fecha")
                    )
                    listaAsistencias.add(asistencia)
                }
            } catch (ex: SQLException) {
                Log.e("DBError", "Error al obtener asistencia: ${ex.message}")
            } finally {
                conn.close()
            }
        } else {
            Log.e("DBError", "No se pudo conectar a la base de datos")
        }
        return listaAsistencias
    }

}
