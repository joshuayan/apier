import org.apache.flink.api.common.typeinfo.BasicTypeInfo
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.ExecutionEnvironment
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.sinks.CsvTableSink
import org.apache.flink.table.sources.CsvTableSource


fun main(args: Array<String>) {
    val env = ExecutionEnvironment.getExecutionEnvironment()

    val tEnv = TableEnvironment.getTableEnvironment(env)
// create a TableSource
    val csvSource = CsvTableSource("hdfs://hadoop-1:9000/user/hive/warehouse/order_item_info/part-m-00003", arrayOf("work_year_month", "pos", "seq_id", "entry_id", "id", "product_id", "price", "quantity", "amount", "remark"), arrayOf<TypeInformation<*>>(BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.BIG_DEC_TYPE_INFO, BasicTypeInfo.BIG_DEC_TYPE_INFO, BasicTypeInfo.BIG_DEC_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO))


// register the TableSource as table "CsvTable"
    tEnv.registerTableSource("orderItemInfo", csvSource)

//        Table table = tEnv.scan("orderItemInfo");

    val tab = tEnv.sql("select sum(price*quantity) as pq,sum(quantity) as q,product_id from orderItemInfo group by product_id")

    val sink = CsvTableSink("hdfs://hadoop-1:9000/user/root/result", "|")
    tab.writeToSink(sink)
    try {
        env.execute("order item env")
    } catch (e: Exception) {
        e.printStackTrace()
    }

}
