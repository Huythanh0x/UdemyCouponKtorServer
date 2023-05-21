package com.example.data.dao

import com.example.data.DatabaseProvider
import com.example.data.model.LogFetchCoupon
import com.example.utils.Constants
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


object LogFetchCouponDao {
    init {
        createTableIfNotExist()
    }

    private fun createTableIfNotExist() {
        val connection = DatabaseProvider().getConnection()
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS ${Constants.logFetchCouponTableName} (
                id INT AUTO_INCREMENT PRIMARY KEY,
                time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                total INT,
                valid INT,
                expired INT,
                error INT,
                currentIpAddress VARCHAR(255)
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
        val sql = "DROP TABLE IF EXISTS ${Constants.logFetchCouponTableName}"
        try {
            statement.executeUpdate(sql)
            statement.close()
            connection.close()
            println("Table ${Constants.logFetchCouponTableName} dropped successfully")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun insertLogFetchCoupon(total: Int, valid: Int, expired: Int, error: Int, currentIpAddress: String) {
        val connection = DatabaseProvider().getConnection()
        val insertQuery = """
            INSERT INTO ${Constants.logFetchCouponTableName} (
                total, valid, expired, error, currentIpAddress
            ) VALUES (?, ?, ?, ?, ?)
        """.trimIndent()

        try {
            val preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, total)
            preparedStatement.setInt(2, valid)
            preparedStatement.setInt(3, expired)
            preparedStatement.setInt(4, error)
            preparedStatement.setString(5, currentIpAddress)

            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
    }

    fun getAllLogFetchCoupons(): Set<LogFetchCoupon> {
        val connection = DatabaseProvider().getConnection()
        val selectQuery = "SELECT * FROM ${Constants.logFetchCouponTableName}"
        val logFetchCoupons = mutableSetOf<LogFetchCoupon>()

        try {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(selectQuery)

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val time = resultSet.getTimestamp("time")
                val total = resultSet.getInt("total")
                val valid = resultSet.getInt("valid")
                val expired = resultSet.getInt("expired")
                val error = resultSet.getInt("error")
                val currentIpAddress = resultSet.getString("currentIpAddress")

                val logFetchCoupon = LogFetchCoupon(id, time, total, valid, expired, error, currentIpAddress)
                logFetchCoupons.add(logFetchCoupon)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

        return logFetchCoupons
    }
}

