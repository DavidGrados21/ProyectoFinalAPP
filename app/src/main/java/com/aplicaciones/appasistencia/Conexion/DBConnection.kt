package com.aplicaciones.appasistencia

import android.os.StrictMode
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DBConnection {

    // Datos de la base de datos
    private const val ip = "ProyectoAsistencia.mssql.somee.com:1433"
    private const val db = "ProyectoAsistencia"
    private const val username = "Nilsonxd_SQLLogin_1"
    private const val password = "fadhnnv52q"

    // Establece la conexión con la base de datos
    fun getConnection(): Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var conn: Connection? = null
        val connString: String

        try {
            // Cargar el driver de la base de datos
            Class.forName("net.sourceforge.jtds.jdbc.Driver")

            // Cadena de conexión
            connString = "jdbc:jtds:sqlserver://$ip;databaseName=$db;user=$username;password=$password;TrustServerCertificate=True"
            conn = DriverManager.getConnection(connString)
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } catch (ex: ClassNotFoundException) {
            ex.printStackTrace()
        }

        return conn
    }
}
