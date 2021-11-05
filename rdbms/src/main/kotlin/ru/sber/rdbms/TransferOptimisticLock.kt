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
                var version = 0
                var currentBalance = 0

                val prepareStatement1 = conn.prepareStatement(
                    "select * from account1 where id = $accountId1")
                prepareStatement1.use { statement ->
                    statement.executeQuery().use {
                        it.next()
                        version = it.getInt("version")
                        currentBalance = it.getInt("amount")
                    }
                }

                if (currentBalance - amount < 0)
                    throw NotEnoughMoneyException()

                val prepareStatement2 = conn.prepareStatement(
                    "update account1 set amount = amount - 100, " +
                            "version = version + 1 where id = $accountId1 and version = ?")
                prepareStatement2.use { statement ->
                    statement.setInt(1, version)
                    val updatedRows = statement.executeUpdate()
                    if (updatedRows == 0)
                        throw ConcurrentUpdateException()
                }

                val prepareStatement3 = conn.prepareStatement(
                    "select * from account1 where id = $accountId2")
                prepareStatement3.use { statement ->
                    statement.executeQuery().use {
                        it.next()
                        version = it.getInt("version")
                    }
                }
                val prepareStatement4 =
                    conn.prepareStatement(
                        "update account1 set amount = amount + $amount, " +
                                "version = version + 1 where id = $accountId2 and version = ?")
                prepareStatement4.use { statement ->
                    statement.setInt(1, version)
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
