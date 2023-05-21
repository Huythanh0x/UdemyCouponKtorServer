package com.example.data.dao

import com.example.data.DatabaseProvider
import com.example.data.model.LogRequestCoupon
import com.example.utils.Constants
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp

object LogRequestCouponDao {
    init {
        createTableIfNotExist()
    }

    private fun createTableIfNotExist() {
        val connection = DatabaseProvider().getConnection()
        val createTableQuery = """
        CREATE TABLE IF NOT EXISTS ${Constants.logRequestCouponTableName} (
            id INT AUTO_INCREMENT PRIMARY KEY,
            ipAddress VARCHAR(255),
            entryPoint VARCHAR(255),
            statusCode INT,
            message VARCHAR(255),
            dateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
    """.trimIndent()

        try {
            val statement = connection.createStatement()
            statement.execute(createTableQuery)
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
    }

    fun dropTable() {
        val connection = DatabaseProvider().getConnection()
        val statement = connection.createStatement()
        val sql = "DROP TABLE IF EXISTS ${Constants.logRequestCouponTableName}"
        try {
            statement.executeUpdate(sql)
            statement.close()
            connection.close()
            println("Table ${Constants.logRequestCouponTableName} dropped successfully")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun insertLogRequestCoupon(ipAddress: String, entryPoint: String, statusCode: Int, message: String) {
        val connection = DatabaseProvider().getConnection()
        val insertQuery = """
        INSERT INTO ${Constants.logRequestCouponTableName} (
            ipAddress, entryPoint, statusCode, message
        ) VALUES (?, ?, ?, ?)
    """.trimIndent()

        try {
            val preparedStatement = connection.prepareStatement(insertQuery)
            preparedStatement.setString(1, ipAddress)
            preparedStatement.setString(2, entryPoint)
            preparedStatement.setInt(3, statusCode)
            preparedStatement.setString(4, message)

            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
    }

    fun getAllLogRequests(): Set<LogRequestCoupon> {
        val connection = DatabaseProvider().getConnection()
        val selectQuery = "SELECT * FROM ${Constants.logRequestCouponTableName}"

        val logRequests = mutableSetOf<LogRequestCoupon>()

        try {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(selectQuery)

            while (resultSet.next()) {
                logRequests.add(getLogRequestCouponFromResultSet(resultSet))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

        return logRequests
    }

    fun getLogRequestsByIpAddress(ipAddress: String): Set<LogRequestCoupon> {
        val connection = DatabaseProvider().getConnection()
        val selectQuery = "SELECT * FROM ${Constants.logRequestCouponTableName} WHERE ipAddress = ?"

        val logRequests = mutableSetOf<LogRequestCoupon>()

        try {
            val preparedStatement = connection.prepareStatement(selectQuery)
            preparedStatement.setString(1, ipAddress)

            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                logRequests.add(getLogRequestCouponFromResultSet(resultSet))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

        return logRequests
    }

    fun getLogRequestsByEntryPoint(entryPoint: String): Set<LogRequestCoupon> {
        val connection = DatabaseProvider().getConnection()
        val selectQuery = "SELECT * FROM ${Constants.logRequestCouponTableName} WHERE entryPoint = ?"

        val logRequests = mutableSetOf<LogRequestCoupon>()

        try {
            val preparedStatement = connection.prepareStatement(selectQuery)
            preparedStatement.setString(1, entryPoint)

            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                logRequests.add(getLogRequestCouponFromResultSet(resultSet))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

        return logRequests
    }

    fun getLogRequestsByStatusCode(statusCode: Int): Set<LogRequestCoupon> {
        val connection = DatabaseProvider().getConnection()
        val selectQuery = "SELECT * FROM ${Constants.logRequestCouponTableName} WHERE statusCode = ?"

        val logRequests = mutableSetOf<LogRequestCoupon>()

        try {
            val preparedStatement = connection.prepareStatement(selectQuery)
            preparedStatement.setInt(1, statusCode)

            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                logRequests.add(getLogRequestCouponFromResultSet(resultSet))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

        return logRequests
    }

    fun getLogRequestsInRange(startTime: Timestamp, endTime: Timestamp): Set<LogRequestCoupon> {
        val connection = DatabaseProvider().getConnection()
        val selectQuery = "SELECT * FROM ${Constants.logRequestCouponTableName} WHERE dateTime BETWEEN ? AND ?"

        val logRequests = mutableSetOf<LogRequestCoupon>()

        try {
            val preparedStatement = connection.prepareStatement(selectQuery)
            preparedStatement.setTimestamp(1, startTime)
            preparedStatement.setTimestamp(2, endTime)

            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                logRequests.add(getLogRequestCouponFromResultSet(resultSet))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
        return logRequests
    }

    private fun getLogRequestCouponFromResultSet(resultSet: ResultSet): LogRequestCoupon {
        val id = resultSet.getInt("id")
        val ipAddress = resultSet.getString("ipAddress")
        val entryPoint = resultSet.getString("entryPoint")
        val statusCode = resultSet.getInt("statusCode")
        val message = resultSet.getString("message")
        val dateTime = resultSet.getTimestamp("dateTime")
        return LogRequestCoupon(id, ipAddress, entryPoint, statusCode, message, dateTime)
    }
}