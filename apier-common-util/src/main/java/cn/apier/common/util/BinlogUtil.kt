package cn.apier.common.util

import org.hibernate.boot.jaxb.SourceType
import java.io.File
import java.util.function.Consumer

/**
 * Created by yanjunhua on 2017/6/2.
 */

fun readFile(fileName: String) {
    val tmpName = "/Users/yanjunhua/tmp/binlog/mysqlbin00067.txt"
    val file = File(tmpName)

//    file.forEachLine { System.out.println(it)  }
    val buffedReader = file.bufferedReader()
//    val cnt = buffedReader.lines().count()
//    print(cnt)
//    buffedReader.forEachLine { println(it) }
    var sqls = mutableListOf<SqlContainer>()

    var currentSqlContainer: SqlContainer = SqlContainer("")

    var newLine = false

    var reg = Regex("""/\*.*\*/""")

    val sqlReg = Regex("""UPDATE|DELETE|INSERT""")
    buffedReader.lines().sequential().filter({ it.contains("""###""") }).map { it.replace("""###""", "") }.map { it.replace(reg, "") }
            .forEach {
                //                println(it)
                if (it.contains(sqlReg)) {
                    var newSqlC = SqlContainer(it)
                    sqls.add(newSqlC)
                    currentSqlContainer = newSqlC
                } else {
                    currentSqlContainer.addSql(it)
                }
            }

//    sqls.forEach { println(it.sql()) }

    val tmpFile = File("/Users/yanjunhua/tmp/binlog/sqls67.txt")
//    tmpFile.deleteOnExit()
    tmpFile.writeText("")
    sqls.stream().parallel().map { it.sql() }.map { it.trim() }.filter { it.contains("price_model") && !it.contains("XXL_JOB") && !it.startsWith("INSERT") && !it.startsWith("UPDATE") }
            .forEach { tmpFile.appendText(it + "\n") }
}

class SqlContainer(val command: String) {
    val sqlList = mutableListOf<String>()
    fun addSql(sql: String) {
        this.sqlList.add(sql)
    }

    fun sql(): String {
        return this.command + this.sqlList.map { " " + it }.reduce { acc, s -> acc + s }.toString()
    }
}


fun main(args: Array<String>) {
    readFile("")
}