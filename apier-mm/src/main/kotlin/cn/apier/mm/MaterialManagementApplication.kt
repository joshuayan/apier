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
    SpringApplication.run(MaterialManagementApplication::class.java, *args)
}
