/**
 * Created by yanjunhua on 2017/6/6.
 */
package cn.apier.standard;

import cn.apier.standard.domain.model.City
import cn.apier.standard.domain.model.Country
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class StandardDataApplication {

}

fun main(args: Array<String>) {
    SpringApplication.run(StandardDataApplication::class.java, *args)
//    test()
}
//
//fun test() {
//    val country = Country("1", "china")
//    println(country.toString())
//    println(country.createdAt)
//    val city = City("2", "1", "city")
//    city.update("name1")
//}