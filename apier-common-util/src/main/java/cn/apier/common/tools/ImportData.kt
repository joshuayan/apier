package cn.apier.common.tools

import java.io.File
import java.util.stream.Collectors

/**
 * Created by yanjunhua on 2017/6/20.
 */


fun importRegionFromFile(filePath: String) {

    val file = File(filePath)
    file.bufferedReader().lines().map<Pair<String, String>> { val items = it.apply { it.trim() }.split(" +".toRegex());Pair<String, String>(items[0].trim(), items[1].trim()) }
            .collect(Collectors.groupingBy<Pair<String, String>, String> { it.first.substring(0..1) })
            .toList().forEach {
        it.second.forEach {
            println("${it.first}-${it.second}");
            var currentProvince = ""
            var currentCity = ""
            when {
                it.first.matches("""^\d{2}0000$""".toRegex()) -> {
                    currentProvince = it.first
                    println("province:${it.second}")
                }
                it.first.matches("""^\d{4}00$""".toRegex()) -> {
                    println("city:${it.second}");currentCity = it.first
                }
                else->{
                    println("district:${it.second}")
                }
            }
        }

//        println(it)
    }
//    Flux.fromIterable<Pair<String,List<Pair<String,String>>>>(lineList).reduceWith({})
//            .reduce(identity:)
//                        .forEach { println(it) }
}

fun main(args: Array<String>) {
    importRegionFromFile("/Users/yanjunhua/tmp/region-2017.txt")
}