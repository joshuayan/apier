package cn.apier.mm

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * Created by yanjunhua on 2017/6/21.
 */

@SpringBootApplication
@EnableTransactionManagement
open class MaterialManagementApplication

fun main(args: Array<String>) {
//    SpringApplication.run(MaterialManagementApplication::class.java, *args)

    test()

}

private fun test() {
    val timeStr="1997-12-13"
            //"10:00:00"

   val result= timeStr.matches("""\d{4}-\d{2}-\d{2}""".toRegex())
    println("result:$result")
}
