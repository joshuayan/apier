package cn.apier.common

import net.sf.jsqlparser.parser.CCJSqlParserUtil


fun main(args: Array<String>) {
   val smt= CCJSqlParserUtil.parse("select * from test1 a join test2 b on a.m=b.n join (select * from test3 c where c.id=1) x on x.id=b.id where  m=2")
    println("smt:$smt")
}