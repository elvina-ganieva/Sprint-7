package main.kotlin.ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val minID: Long
        val maxID: Long

        if (accountId1 > accountId2) {
            minID = accountId1
            maxID = accountId2
        } else {
            minID = accountId2
            maxID = accountId1
        }

        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val autoCommit = conn.autoCommit
            try {
                conn.autoCommit = false
                val prepareStatement1 =
                    conn.prepareStatement("select * from account1 where id = ? for update")
                prepareStatement1.use { statement ->
                    statement.setLong(1, minID)
                    statement.executeQuery()
                }
                val prepareStatement2 =
                    conn.prepareStatement("select * from account1 where id = ? for update")
                prepareStatement2.use { statement ->
                    statement.setLong(1, maxID)
                    statement.executeQuery()
                }

                val prepareStatement3 =
                    conn.prepareStatement("update account1 set amount = amount - ? where id = ?")
                prepareStatement3.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.executeUpdate()
                }
                val prepareStatement4 =
                    conn.prepareStatement("update account1 set amount = amount + ? where id = ?")
                prepareStatement4.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.executeUpdate()
                }
                conn.commit()
            } catch (exception: SQLException) {
                println(exception.message)
                conn.rollback()
            } finally {
                conn.autoCommit = autoCommit
            }
        }
    }
}