package com.example.data.dao

import com.example.data.DatabaseProvider
import com.example.data.model.CouponCourseData
import com.example.utils.Constants
import java.sql.ResultSet
import java.sql.SQLException

object CouponDAO {
    init {
        createTableIfNotExists()
    }

    private fun createTableIfNotExists() {
        val connection = DatabaseProvider().getConnection()
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS ${Constants.couponTableName} (
                courseId INT PRIMARY KEY,
                category VARCHAR(255),
                subCategory VARCHAR(255),
                title VARCHAR(255),
                contentLength INT,
                level VARCHAR(255),
                author VARCHAR(255),
                rating FLOAT,
                reviews INT,
                students INT,
                couponCode VARCHAR(255),
                previewImage VARCHAR(255),
                couponUrl VARCHAR(255),
                expiredDate VARCHAR(255),
                usesRemaining INT,
                heading VARCHAR(255),
                description TEXT,
                previewVideo VARCHAR(255),
                language VARCHAR(255)
            )
        """.trimIndent()

        try {
            connection.createStatement()?.execute(createTableQuery)
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
    }

    fun dropTable() {
        val connection = DatabaseProvider().getConnection()
        val dropTableQuery = "DROP TABLE IF EXISTS ${Constants.couponTableName}"
        try {
            connection.createStatement()?.executeUpdate(dropTableQuery)
            println("Table ${Constants.couponTableName} dropped successfully")
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

    }

    fun insertCouponCourses(couponCourses: Set<CouponCourseData>) {
        val connection = DatabaseProvider().getConnection()
        val insertQuery = """
            INSERT INTO ${Constants.couponTableName} (
                courseId, category, subCategory, title, contentLength, level,
                author, rating, reviews, students, couponCode, previewImage,
                couponUrl, expiredDate, usesRemaining, heading, description,
                previewVideo, language
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        try {
            val preparedStatement = connection.prepareStatement(insertQuery)
            for (couponCourse in couponCourses) {
                preparedStatement.setInt(1, couponCourse.courseId)
                preparedStatement.setString(2, couponCourse.category)
                preparedStatement.setString(3, couponCourse.subCategory)
                preparedStatement.setString(4, couponCourse.title)
                preparedStatement.setInt(5, couponCourse.contentLength)
                preparedStatement.setString(6, couponCourse.level)
                preparedStatement.setString(7, couponCourse.author)
                preparedStatement.setFloat(8, couponCourse.rating)
                preparedStatement.setInt(9, couponCourse.reviews)
                preparedStatement.setInt(10, couponCourse.students)
                preparedStatement.setString(11, couponCourse.couponCode)
                preparedStatement.setString(12, couponCourse.previewImage)
                preparedStatement.setString(13, couponCourse.couponUrl)
                preparedStatement.setString(14, couponCourse.expiredDate)
                preparedStatement.setInt(15, couponCourse.usesRemaining)
                preparedStatement.setString(16, couponCourse.heading)
                preparedStatement.setString(17, couponCourse.description)
                preparedStatement.setString(18, couponCourse.previewVideo)
                preparedStatement.setString(19, couponCourse.language)

                preparedStatement.addBatch()
            }
            preparedStatement.executeBatch()
            preparedStatement.close()

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
    }

    fun getAllCouponCourses(): Set<CouponCourseData> {
        val connection = DatabaseProvider().getConnection()
        val couponCourses = mutableSetOf<CouponCourseData>()
        val getAllCouponsQuery = "SELECT * FROM ${Constants.couponTableName}"
        try {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(getAllCouponsQuery)
            while (resultSet.next()) {
                couponCourses.add(getCouponCourseFromResultQuery(resultSet))
            }
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
        return couponCourses
    }


    fun getNCouponCourses(numberOfCourseRequest: Int): Set<CouponCourseData> {
        val connection = DatabaseProvider().getConnection()
        val couponCourses = mutableSetOf<CouponCourseData>()
        val getAllCouponsQuery = "SELECT * FROM ${Constants.couponTableName} LIMIT $numberOfCourseRequest"
        try {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(getAllCouponsQuery)
            while (resultSet.next()) {
                couponCourses.add(getCouponCourseFromResultQuery(resultSet))
            }
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
        return couponCourses
    }

    fun searchCouponsByKeyword(keyword: String): Set<CouponCourseData> {
        val query = "SELECT * FROM ${Constants.couponTableName} WHERE title LIKE ? OR description LIKE ?"
        val wildcardKeyword = "%$keyword%"
        val connection = DatabaseProvider().getConnection()
        val preparedStatement = connection.prepareStatement(query)

        preparedStatement.setString(1, wildcardKeyword)
        preparedStatement.setString(2, wildcardKeyword)

        val resultSet = preparedStatement.executeQuery()
        val couponCourses = mutableSetOf<CouponCourseData>()

        while (resultSet.next()) {
            couponCourses.add(getCouponCourseFromResultQuery(resultSet))
        }
        resultSet.close()
        preparedStatement.close()
        connection.close()

        return couponCourses
    }

    fun deleteCouponCourse(couponUrl: String) {
        val connection = DatabaseProvider().getConnection()
        val deleteQuery = "DELETE FROM ${Constants.couponTableName} WHERE couponUrl = ?"
        try {
            val preparedStatement = connection.prepareStatement(deleteQuery)
            preparedStatement.setString(1, couponUrl)
            preparedStatement.executeUpdate()
            preparedStatement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        connection.close()
    }

    fun deleteAllCouponCourses() {
        val connection = DatabaseProvider().getConnection()
        val deleteQuery = "DELETE FROM ${Constants.couponTableName}"
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(deleteQuery)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        connection.close()
    }

    private fun getCouponCourseFromResultQuery(resultSet: ResultSet): CouponCourseData {
        val courseId = resultSet.getInt("courseId")
        val category = resultSet.getString("category")
        val subCategory = resultSet.getString("subCategory")
        val title = resultSet.getString("title")
        val contentLength = resultSet.getInt("contentLength")
        val level = resultSet.getString("level")
        val author = resultSet.getString("author")
        val rating = resultSet.getFloat("rating")
        val reviews = resultSet.getInt("reviews")
        val students = resultSet.getInt("students")
        val couponCode = resultSet.getString("couponCode")
        val previewImage = resultSet.getString("previewImage")
        val couponUrl = resultSet.getString("couponUrl")
        val expiredDate = resultSet.getString("expiredDate")
        val usesRemaining = resultSet.getInt("usesRemaining")
        val heading = resultSet.getString("heading")
        val description = resultSet.getString("description")
        val previewVideo = resultSet.getString("previewVideo")
        val language = resultSet.getString("language")

        return CouponCourseData(
            courseId,
            category,
            subCategory,
            title,
            contentLength,
            level,
            author,
            rating,
            reviews,
            students,
            couponCode,
            previewImage,
            couponUrl,
            expiredDate,
            usesRemaining,
            heading,
            description,
            previewVideo,
            language
        )
    }
}