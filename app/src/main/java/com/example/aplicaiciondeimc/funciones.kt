package com.example.aplicaiciondeimc


import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.util.Date
class funciones {


    object OracleDBOperations {

        fun insertIMCData(connection: Connection, peso:Double,altura:Int,sexo:String,edad:Int) {
            val sql = "INSERT INTO DatosIMC (peso, altura, sexo, edad) VALUES (?, ?, ?,?)"
            try {
                connection.prepareStatement(sql).use { statement ->

                    statement.setDouble(1, peso)
                    statement.setInt(2, altura)
                    statement.setString(3, sexo)
                    statement.setInt(4,edad)
                }
            } catch (e: SQLException) {

            }
        }

        fun retrieveIMCData(connection: Connection): List<DatosIMC> {
            val sql = "SELECT * FROM IMC"
            val dataList = mutableListOf<DatosIMC>()
            try {
                connection.prepareStatement(sql).use { statement ->
                    statement.executeQuery().use { resultSet ->
                        while (resultSet.next()) {
                            val id = resultSet.getInt("id")
                            val peso = resultSet.getDouble("peso")
                            val altura=resultSet.getInt("altura")
                            val sexo=resultSet.getString("sexo")
                            val edad=resultSet.getInt("edad")

                            val imcData = DatosIMC(id, peso =peso,altura=altura,sexo=sexo,edad=edad )
                            dataList.add(imcData)
                        }
                    }
                }
            } catch (e: SQLException) {
                // Manejar la excepción
            }
            return dataList
        }
    }

    data class DatosIMC(val id: Int, val peso: Double,val altura: Int,val sexo: String,val edad: Int)

}