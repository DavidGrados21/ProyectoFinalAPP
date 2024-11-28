package com.aplicaciones.appasistencia.Datos

import android.util.Log
import com.aplicaciones.appasistencia.DBConnection
import com.aplicaciones.appasistencia.Entidad.Curso
import java.sql.SQLException
import java.sql.Statement

class DatCurso {

    // Método para insertar un curso
    fun insertarCurso(curso: Curso): String {
        val conn = DBConnection.getConnection() // Usar la conexión de DBConnection
        if (conn != null) {
            try {
                val sql = """
                    INSERT INTO Cursos (NombreCurso, Descripcion, CodigoCurso, ProfesorID, HoraInicio, HoraFin, DiaSemana)
                    VALUES ('${curso.nombreCurso}', '${curso.descripcion}', '${curso.codigoCurso}', ${curso.profesorId}, '${curso.horaInicio}', '${curso.horaFin}', '${curso.diaSemana}')
                """
                val statement: Statement = conn.createStatement()
                val rowsAffected = statement.executeUpdate(sql)

                if (rowsAffected > 0) {
                    return "Curso registrado exitosamente"
                } else {
                    return "Error al registrar el curso"
                }
            } catch (ex: SQLException) {
                ex.printStackTrace()
                return "Error al registrar el curso: ${ex.message}"
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

    // Método para obtener un curso por su ID
    fun obtenerCursoPorId(cursoId: Int): Curso? {
        val conn = DBConnection.getConnection()
        if (conn != null) {
            try {
                val sql = "SELECT * FROM Cursos WHERE CursoID = $cursoId"
                val statement = conn.createStatement()
                val resultSet = statement.executeQuery(sql)

                if (resultSet.next()) {
                    return Curso(
                        id = resultSet.getInt("CursoID"),
                        nombreCurso = resultSet.getString("NombreCurso"),
                        descripcion = resultSet.getString("Descripcion"),
                        codigoCurso = resultSet.getString("CodigoCurso"),
                        profesorId = resultSet.getInt("ProfesorID"),
                        horaInicio = resultSet.getString("HoraInicio"),
                        horaFin = resultSet.getString("HoraFin"),
                        diaSemana = resultSet.getString("DiaSemana")
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
        return null
    }

    // Método para obtener todos los cursos de un profesor
    fun obtenerCursosPorProfesorId(profesorId: Int): List<Curso> {
        val conn = DBConnection.getConnection()
        val cursos = mutableListOf<Curso>()

        if (conn != null) {
            try {
                val sql = "SELECT * FROM Cursos WHERE ProfesorID = $profesorId"
                val statement = conn.createStatement()
                val resultSet = statement.executeQuery(sql)

                while (resultSet.next()) {
                    val curso = Curso(
                        id = resultSet.getInt("CursoID"),
                        nombreCurso = resultSet.getString("NombreCurso"),
                        descripcion = resultSet.getString("Descripcion"),
                        codigoCurso = resultSet.getString("CodigoCurso"),
                        profesorId = resultSet.getInt("ProfesorID"),
                        horaInicio = resultSet.getString("HoraInicio"),
                        horaFin = resultSet.getString("HoraFin"),
                        diaSemana = resultSet.getString("DiaSemana")
                    )
                    cursos.add(curso)
                }
            } catch (ex: SQLException) {
                Log.e("DatCurso", "Error SQL: ${ex.message}")
                ex.printStackTrace()
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatCurso", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        }

        return cursos // Devuelve la lista de cursos, que puede estar vacía
    }

    // Método para actualizar un curso
    fun actualizarCurso(curso: Curso): String {
        val conn = DBConnection.getConnection()
        if (conn != null) {
            try {
                val sql = """
                    UPDATE Cursos 
                    SET NombreCurso = ?, Descripcion = ?, CodigoCurso = ?, ProfesorID = ?, HoraInicio = ?, HoraFin = ?, DiaSemana = ?
                    WHERE CursoID = ?
                """
                val preparedStatement = conn.prepareStatement(sql)

                preparedStatement.setString(1, curso.nombreCurso)
                preparedStatement.setString(2, curso.descripcion)
                preparedStatement.setString(3, curso.codigoCurso)
                preparedStatement.setInt(4, curso.profesorId)
                preparedStatement.setString(5, curso.horaInicio)
                preparedStatement.setString(6, curso.horaFin)
                preparedStatement.setString(7, curso.diaSemana)
                preparedStatement.setInt(8, curso.id)

                val rowsAffected = preparedStatement.executeUpdate()
                return if (rowsAffected > 0) {
                    "Curso actualizado exitosamente"
                } else {
                    "No se encontraron datos para actualizar"
                }
            } catch (ex: SQLException) {
                Log.e("DatCurso", "Error SQL: ${ex.message}")
                ex.printStackTrace()
                return "Error al actualizar el curso: ${ex.message}"
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatCurso", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        return "Error de conexión"
    }
    fun obtenerCursoConProfesorPorId(cursoId: Int): Curso? {
        val conn = DBConnection.getConnection() // Usar la conexión de DBConnection
        if (conn != null) {
            try {
                // Consulta SQL para obtener el curso junto con el nombre del profesor
                val sql = """
                SELECT 
                    Cursos.CursoID, 
                    Cursos.NombreCurso, 
                    Cursos.Descripcion, 
                    Cursos.CodigoCurso, 
                    Cursos.ProfesorID, 
                    Cursos.HoraInicio, 
                    Cursos.HoraFin, 
                    Cursos.DiaSemana, 
                    Profesores.Nombre + ' ' + Profesores.Apellido AS NombreProfesor
                FROM Cursos
                INNER JOIN Profesores ON Cursos.ProfesorID = Profesores.ProfesorID
                WHERE Cursos.CursoID = ?
            """
                // Preparar la sentencia con parámetro
                val preparedStatement = conn.prepareStatement(sql)
                preparedStatement.setInt(1, cursoId)

                // Ejecutar la consulta
                val resultSet = preparedStatement.executeQuery()

                // Verificar si hay resultados
                if (resultSet.next()) {
                    // Crear y devolver el objeto Curso con la información obtenida
                    return Curso(
                        id = resultSet.getInt("CursoID"),
                        nombreCurso = resultSet.getString("NombreCurso"),
                        descripcion = resultSet.getString("Descripcion"),
                        codigoCurso = resultSet.getString("CodigoCurso"),
                        profesorId = resultSet.getInt("ProfesorID"),
                        horaInicio = resultSet.getString("HoraInicio"),
                        horaFin = resultSet.getString("HoraFin"),
                        diaSemana = resultSet.getString("DiaSemana"),
                        nombreProfesor = resultSet.getString("NombreProfesor") // Nombre del profesor
                    )
                }
            } catch (ex: SQLException) {
                Log.e("DatCurso", "Error SQL: ${ex.message}")
                ex.printStackTrace()
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatCurso", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        return null // Si no se encontró el curso, se retorna null
    }
    fun obtenerCursosParaAlumno(alumnoId: Int): List<Curso> {
        val conn = DBConnection.getConnection()
        val cursos = mutableListOf<Curso>()

        if (conn != null) {
            try {
                val sql = """
                SELECT 
                    c.CursoID, 
                    c.NombreCurso, 
                    c.Descripcion, 
                    c.CodigoCurso, 
                    c.ProfesorID, 
                    c.HoraInicio, 
                    c.HoraFin, 
                    c.DiaSemana, 
                    p.Nombre + ' ' + p.Apellido AS NombreProfesor
                FROM Cursos c
                INNER JOIN AlumnoPorCurso apc ON c.CursoID = apc.CursoID
                INNER JOIN Profesores p ON c.ProfesorID = p.ProfesorID
                WHERE apc.AlumnoID = ?
            """
                val preparedStatement = conn.prepareStatement(sql)
                preparedStatement.setInt(1, alumnoId)
                val resultSet = preparedStatement.executeQuery()

                while (resultSet.next()) {
                    val curso = Curso(
                        id = resultSet.getInt("CursoID"),
                        nombreCurso = resultSet.getString("NombreCurso"),
                        descripcion = resultSet.getString("Descripcion"),
                        codigoCurso = resultSet.getString("CodigoCurso"),
                        profesorId = resultSet.getInt("ProfesorID"),
                        horaInicio = resultSet.getString("HoraInicio"),
                        horaFin = resultSet.getString("HoraFin"),
                        diaSemana = resultSet.getString("DiaSemana"),
                        nombreProfesor = resultSet.getString("NombreProfesor") // Profesor asignado
                    )
                    cursos.add(curso)
                }
            } catch (ex: SQLException) {
                Log.e("DatCurso", "Error SQL: ${ex.message}")
                ex.printStackTrace()
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatCurso", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        return cursos
    }

}
