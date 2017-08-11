package cn.apier.stream.flink;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.sinks.CsvTableSink;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.sources.CsvTableSource;

public class QueryOrderItemApplication
{

    public static void main(String[] args)
    {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        BatchTableEnvironment tEnv = TableEnvironment.getTableEnvironment(env);
// create a TableSource
        System.out.println("go 1");
        CsvTableSource csvSource = new CsvTableSource("hdfs://hadoop-1:9000/user/hive/warehouse/order_item_info/part-m-00003", new String[]{"work_year_month", "pos", "seq_id", "entry_id", "id"
                , "product_id", "price", "quantity", "amount", "remark"}, new TypeInformation[]{BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO
                , BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.BIG_DEC_TYPE_INFO
                , BasicTypeInfo.BIG_DEC_TYPE_INFO, BasicTypeInfo.BIG_DEC_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO});


// register the TableSource as table "CsvTable"
        tEnv.registerTableSource("orderItemInfo", csvSource);

        System.out.println("go 2");
//        Table table = tEnv.scan("orderItemInfo");

        Table tab = tEnv.sql("select sum(price*quantity) as pq,sum(quantity) as q,product_id from orderItemInfo group by product_id");

        System.out.println("go 3");
        System.out.println("tab:"+tab.toString());
        TableSink sink = new CsvTableSink("hdfs://hadoop-1:9000/user/root/result", "|");
        tab.writeToSink(sink);
        try
        {
            env.execute("order item env");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("go 4");
    }
}
