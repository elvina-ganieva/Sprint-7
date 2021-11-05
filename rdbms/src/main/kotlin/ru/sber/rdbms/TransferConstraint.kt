package main.kotlin.ru.sber.rdbms

import org.postgresql.util.PSQLException
import java.sql.DriverManager

class TransferConstraint {

    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )

        connection.use { con ->
            try {
                con.prepareStatement(
                    "alter table account1 add constraint amount_check check (amount >= 0);"
                ).use {
                    it.executeUpdate()
                }

                val prepareStatement = con.prepareStatement(
                    "update account1 set amount = amount - $amount where id = $accountId1;" +
                            "update account1 set amount = amount + $amount where id = $accountId2;"
                )
                prepareStatement.use {
                    it.executeUpdate()
                }

                con.prepareStatement(
                    "alter table account1 drop constraint amount_check"
                ).use {
                    it.executeUpdate()
                }
            } catch (ex: PSQLException) {
                println(ex.message)
            }
        }
    }
}
