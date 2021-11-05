package main.kotlin.ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class NotEnoughMoneyException : SQLException() {
    override val message: String
        get() = "Not enough money"
}

class ConcurrentUpdateException : SQLException() {
    override val message: String
        get() = "Concurrent update"
}

class TransferOptimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                var version1 = 0
                var version2 = 0
                var currentBalance = 0

                val prepareStatement1 = conn.prepareStatement(
                    "select * from account1 where id = ?")
                prepareStatement1.use { statement ->
                    statement.setLong(1, accountId1)
                    statement.executeQuery().use {
                        it.next()
                        version1 = it.getInt("version")
                        currentBalance = it.getInt("amount")
                    }
                }

                if (currentBalance - amount < 0)
                    throw NotEnoughMoneyException()

                val prepareStatement2 = conn.prepareStatement(
                    "select * from account1 where id = ?")
                prepareStatement2.use { statement ->
                    statement.setLong(1, accountId2)
                    statement.executeQuery().use {
                        it.next()
                        version2 = it.getInt("version")
                    }
                }

                val prepareStatement3 = conn.prepareStatement(
                    "update account1 set amount = amount - ?, version = version + 1 where id = ? and version = ?")
                prepareStatement3.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.setInt(3, version1)
                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0)
                        throw ConcurrentUpdateException()
                }

                val prepareStatement4 =
                    conn.prepareStatement(
                        "update account1 set amount = amount + ?, version = version + 1 where id = ? and version = ?")
                prepareStatement4.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.setInt(3, version2)
                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0)
                        throw ConcurrentUpdateException()
                }
                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                exception.printStackTrace()
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}
