package com.aplicaciones.appasistencia.Datos
import android.util.Log
import com.aplicaciones.appasistencia.DBConnection
import com.aplicaciones.appasistencia.Entidad.Alumno
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import java.sql.ResultSet
class DatAlumno {
    // Método para insertar un alumno
    fun insertarAlumno(alumno: Alumno): String {
        val conn = DBConnection.getConnection() // Usar la conexión de DBConnection
        if (conn != null) {
            try {
                val sql = "INSERT INTO Alumnos (Nombre, Apellido, Correo, Contraseña, Telefono, Foto) VALUES ('${alumno.nombre}', '${alumno.apellido}', '${alumno.correo}', '${alumno.contraseña}', '${alumno.telefono}', '${alumno.foto}')"
                val statement: Statement = conn.createStatement()
                val rowsAffected = statement.executeUpdate(sql)

                if (rowsAffected > 0) {
                    return "Alumno registrado exitosamente"
                } else {
                    return "Error al registrar al alumno"
                }
            } catch (ex: SQLException) {
                // Verificar si el error es de violación de clave única en SQL Server
                if (ex.sqlState == "23000" || ex.errorCode == 2627 || ex.errorCode == 2601) {
                    return "El correo ya está registrado, porfavor intente con otro"
                } else {
                    // Otro tipo de error
                    return "Error al registrar al alumno: ${ex.message}"
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
    fun obtenerAlumnoPorId(id: Int): Alumno? {
        Log.d("DatAlumno", "Obteniendo alumno con ID: $id")
        val conn = DBConnection.getConnection() // Usar la conexión de DBConnection
        if (conn != null) {
            try {
                val sql = "SELECT * FROM Alumnos WHERE AlumnoID = $id"  // Cambia si la columna no es 'AlumnoID'
                Log.d("DatAlumno", "Consulta SQL: $sql")
                val statement: Statement = conn.createStatement()
                val resultSet: ResultSet = statement.executeQuery(sql)

                if (resultSet.next()) {
                    // Crear un objeto Alumno con los datos obtenidos de la base de datos
                    Log.d("DatAlumno", "Datos obtenidos del alumno: ${resultSet.getString("Nombre")}")
                    return Alumno(
                        id = resultSet.getInt("AlumnoID"),
                        nombre = resultSet.getString("Nombre"),
                        apellido = resultSet.getString("Apellido"),
                        correo = resultSet.getString("Correo"),
                        contraseña = resultSet.getString("Contraseña"),
                        telefono = resultSet.getString("Telefono"),
                        foto = resultSet.getString("Foto"),
                        fechaRegistro = resultSet.getString("FechaRegistro")
                    )

                } else {
                    Log.d("DatAlumno", "No se encontraron resultados para el ID: $id")
                }
            } catch (ex: SQLException) {
                Log.e("DatAlumno", "Error SQL: ${ex.message}")
                ex.printStackTrace()
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatAlumno", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        } else {
            Log.e("DatAlumno", "Error de conexión con la base de datos")
        }
        return null // Si no se encuentra el alumno o hay un error
    }
    fun actualizarAlumno(id: Int, nombre: String, apellido: String, telefono: String, imagen: String? = null): String {
        val conn = DBConnection.getConnection() // Obtener conexión desde DBConnection
        if (conn != null) {
            try {
                // Construcción dinámica de la consulta SQL
                val sql = if (imagen != null) {
                    """
                UPDATE Alumnos 
                SET Nombre = ?, Apellido = ?, Telefono = ?, Foto = ?
                WHERE AlumnoID = ?
                """
                } else {
                    """
                UPDATE Alumnos 
                SET Nombre = ?, Apellido = ?, Telefono = ?
                WHERE AlumnoID = ?
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
                    "Alumno actualizado exitosamente"
                } else {
                    "No se encontraron datos para actualizar"
                }
            } catch (ex: SQLException) {
                Log.e("DatAlumno", "Error SQL: ${ex.message}")
                ex.printStackTrace()
                return "Error al actualizar al alumno: ${ex.message}"
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatAlumno", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        return "Error de conexión"
    }
    fun obtenerAlumnosPorCurso(cursoID: Int): List<Alumno> {
        val alumnos = mutableListOf<Alumno>()
        val conn = DBConnection.getConnection() // Usar la conexión de DBConnection

        if (conn != null) {
            try {
                val sql = """
                SELECT a.* 
                FROM Alumnos a
                INNER JOIN AlumnoPorCurso apc ON a.AlumnoID = apc.AlumnoID
                WHERE apc.CursoID = $cursoID
            """
                val statement: Statement = conn.createStatement()
                val resultSet: ResultSet = statement.executeQuery(sql)

                while (resultSet.next()) {
                    // Crear un objeto Alumno con los datos obtenidos de la base de datos
                    val alumno = Alumno(
                        id = resultSet.getInt("AlumnoID"),
                        nombre = resultSet.getString("Nombre"),
                        apellido = resultSet.getString("Apellido"),
                        correo = resultSet.getString("Correo"),
                        contraseña = resultSet.getString("Contraseña"),
                        telefono = resultSet.getString("Telefono"),
                        foto = resultSet.getString("Foto"),
                        fechaRegistro = resultSet.getString("FechaRegistro")
                    )
                    alumnos.add(alumno)
                }
            } catch (ex: SQLException) {
                Log.e("DatAlumno", "Error SQL: ${ex.message}")
                ex.printStackTrace()
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatAlumno", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        } else {
            Log.e("DatAlumno", "Error de conexión con la base de datos")
        }

        return alumnos // Retornar la lista de alumnos
    }
    fun buscarAlumnosPorNombreApellidoYCurso(busqueda: String, cursoId: Int): List<Alumno> {
        val alumnos = mutableListOf<Alumno>()
        val conn = DBConnection.getConnection() // Usar la conexión de DBConnection

        if (conn != null) {
            try {
                val sql = """
                SELECT a.*
                FROM Alumnos a
                JOIN AlumnoPorCurso ac ON a.AlumnoID = ac.AlumnoID
                WHERE (a.Nombre LIKE '%$busqueda%' OR a.Apellido LIKE '%$busqueda%')
                AND ac.CursoID = $cursoId
            """
                val statement: Statement = conn.createStatement()
                val resultSet: ResultSet = statement.executeQuery(sql)

                while (resultSet.next()) {
                    // Crear un objeto Alumno con los datos obtenidos de la base de datos
                    val alumno = Alumno(
                        id = resultSet.getInt("AlumnoID"),
                        nombre = resultSet.getString("Nombre"),
                        apellido = resultSet.getString("Apellido"),
                        correo = resultSet.getString("Correo"),
                        contraseña = resultSet.getString("Contraseña"),
                        telefono = resultSet.getString("Telefono"),
                        foto = resultSet.getString("Foto"),
                        fechaRegistro = resultSet.getString("FechaRegistro")
                    )
                    alumnos.add(alumno)
                }
            } catch (ex: SQLException) {
                Log.e("DatAlumno", "Error SQL: ${ex.message}")
                ex.printStackTrace()
            } finally {
                try {
                    conn.close()
                } catch (e: SQLException) {
                    Log.e("DatAlumno", "Error al cerrar la conexión: ${e.message}")
                    e.printStackTrace()
                }
            }
        } else {
            Log.e("DatAlumno", "Error de conexión con la base de datos")
        }

        return alumnos // Retornar la lista de alumnos encontrados
    }




}