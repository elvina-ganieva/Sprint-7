package main.kotlin.ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )

        connection.use { con ->
            try {
                val prepareStatement1 = con.prepareStatement(
                    "update account1 set amount = amount - ? where id = ?;"
                )
                prepareStatement1.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.executeUpdate()
                }

                val prepareStatement2 = con.prepareStatement(
                    "update account1 set amount = amount + ? where id = ?;"
                )
                prepareStatement2.use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.executeUpdate()
                }

            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
    }
}
