package com.example.aplicaiciondeimc
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
class conexion {

    object OracleDBConnection {

        fun getConnection(): Connection {
            try {
                Class.forName("oracle.jdbc.OracleDriver")
                val jdbcUrl = "jdbc:oracle:thin:@//172.15.2.191:1521/xe"
                val user = "SYSTEM"
                val password = "1234"
                Log.i("gabriel","hastaca")
                return DriverManager.getConnection(jdbcUrl, user, password)
            } catch (e: ClassNotFoundException) {
                throw SQLException("Oracle JDBC Driver no encontrado", e)
            } catch (e: SQLException) {
                throw SQLException("Error al establecer la conexión", e)
            }
        }
    }
}