package com.example.data.dao

import com.example.data.DatabaseProvider
import com.example.data.model.ExpiredCoupon
import com.example.utils.Constants
import java.sql.ResultSet
import java.sql.SQLException

object ExpiredCouponDAO {
    init {
        createTableIfNotExists()
    }

    private fun createTableIfNotExists() {
        val connection = DatabaseProvider().getConnection()
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS ${Constants.expiredCouponTableName} (
                couponUrl VARCHAR(255) PRIMARY KEY,
                timeStamp TIMESTAMP
            )
        """.trimIndent()

        try {
            val statement = connection.createStatement()
            statement.execute(createTableQuery)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun dropTable() {
        val connection = DatabaseProvider().getConnection()
        val statement = connection.createStatement()
        val sql = "DROP TABLE IF EXISTS ${Constants.expiredCouponTableName}"
        try {
            statement.executeUpdate(sql)
            statement.close()
            connection.close()
            println("Table ${Constants.expiredCouponTableName} dropped successfully")
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    fun insertExpiredCoupons(expiredCoupons: Set<ExpiredCoupon>) {
        val connection = DatabaseProvider().getConnection()
        val insertQuery = """
        INSERT INTO ${Constants.expiredCouponTableName} (couponUrl, timeStamp)
        SELECT ?, ? FROM DUAL
        WHERE NOT EXISTS (
            SELECT 1 FROM ${Constants.expiredCouponTableName} WHERE couponUrl = ?
        )
    """.trimIndent()

        try {
            val preparedStatement = connection.prepareStatement(insertQuery)
            for (expiredCoupon in expiredCoupons) {
                preparedStatement.setString(1, expiredCoupon.couponUrl)
                preparedStatement.setTimestamp(2, expiredCoupon.timeStamp)
                preparedStatement.setTimestamp(3, expiredCoupon.timeStamp)
                preparedStatement.addBatch()
            }
            preparedStatement.executeBatch()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getAllExpiredCoupons(): Set<ExpiredCoupon> {
        val connection = DatabaseProvider().getConnection()
        val expiredCoupons = mutableSetOf<ExpiredCoupon>()
        val getAllCouponsQuery = "SELECT * FROM ${Constants.expiredCouponTableName}"
        try {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(getAllCouponsQuery)
            while (resultSet.next()) {
                expiredCoupons.add(getExpiredCouponFromResultQuery(resultSet))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
        return expiredCoupons
    }

    fun deleteExpiredCoupons() {
        val connection = DatabaseProvider().getConnection()
        val deleteQuery = "DELETE FROM ${Constants.expiredCouponTableName} WHERE timeStamp < DATE_SUB(NOW(), INTERVAL 1 WEEK);"
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(deleteQuery)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun getExpiredCouponFromResultQuery(resultSet: ResultSet): ExpiredCoupon {
        val timeStamp = resultSet.getTimestamp("timeStamp")
        val couponUrl = resultSet.getString("couponUrl")
        return ExpiredCoupon(couponUrl,timeStamp)
    }
}