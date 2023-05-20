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

    fun insertLogFetchCoupon(logFetchCoupon: LogFetchCoupon) {
        val connection = DatabaseProvider().getConnection()
        val insertQuery = """
            INSERT INTO ${Constants.logFetchCouponTableName} (
                time, total, valid, expired, error, currentIpAddress
            ) VALUES (?, ?, ?, ?, ?, ?)
        """.trimIndent()

        try {
            val preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)
            preparedStatement.setTimestamp(1, logFetchCoupon.time)
            preparedStatement.setInt(2, logFetchCoupon.total)
            preparedStatement.setInt(3, logFetchCoupon.valid)
            preparedStatement.setInt(4, logFetchCoupon.expired)
            preparedStatement.setInt(5, logFetchCoupon.error)
            preparedStatement.setString(6, logFetchCoupon.currentIpAddress)

            preparedStatement.executeUpdate()

            val generatedKeys: ResultSet = preparedStatement.generatedKeys
            if (generatedKeys.next()) {
                val generatedId = generatedKeys.getInt(1)
                logFetchCoupon.id = generatedId
            }
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

