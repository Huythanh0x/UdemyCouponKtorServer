package com.example.data

import com.example.utils.Constants
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class DatabaseProvider() {
    init {
        createDatabaseIfNotExists()
    }

    fun getConnection(): Connection {
        return DriverManager.getConnection(Constants.dbUrl, Constants.dbUser, Constants.dbPassword)
    }

    private fun createDatabaseIfNotExists() {

        // Connect to the MySQL server
        val connection: Connection =
            DriverManager.getConnection(Constants.baseDBUrl, Constants.dbUser, Constants.dbPassword)
        val statement: Statement = connection.createStatement()

        // Check if the database already exists
        val checkQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '${Constants.dbName}'"
        val resultSet = statement.executeQuery(checkQuery)

        if (!resultSet.next()) {
            val createQuery = "CREATE DATABASE ${Constants.dbName}"
            statement.executeUpdate(createQuery)
            println("Database '${Constants.dbName}' created successfully.")
        } else {
            println("Database '${Constants.dbName}' already exists.")
        }

        // Close the resources
        resultSet.close()
        statement.close()
        connection.close()
    }
}