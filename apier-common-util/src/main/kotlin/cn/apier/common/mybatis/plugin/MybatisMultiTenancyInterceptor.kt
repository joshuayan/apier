package cn.apier.common.mybatis.plugin

import net.sf.jsqlparser.expression.StringValue
import net.sf.jsqlparser.expression.operators.relational.EqualsTo
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.schema.Table
import net.sf.jsqlparser.statement.select.PlainSelect
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.util.SelectUtils
import org.apache.ibatis.cache.CacheKey
import org.apache.ibatis.executor.Executor
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.plugin.*
import org.apache.ibatis.session.ResultHandler
import org.apache.ibatis.session.RowBounds
import org.slf4j.LoggerFactory
import java.util.*

@Intercepts(
        *arrayOf(
                Signature(type = Executor::class, method = "query", args = arrayOf(MappedStatement::class, Any::class, RowBounds::class, ResultHandler::class)),
                Signature(type = Executor::class, method = "query", args = arrayOf(MappedStatement::class, Any::class, RowBounds::class, ResultHandler::class, CacheKey::class, BoundSql::class)
                )))

class MybatisMultiTenancyInterceptor : Interceptor {

    private val multiTenancyConfiguration = MultiTenancyConfiguration()

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MybatisMultiTenancyInterceptor::class.java)
    }

    override fun intercept(invocation: Invocation): Any {

        val args = invocation.args
        val ms = args[0] as MappedStatement
        val parameter = args[1]
        val rowBounds = args[2] as RowBounds
        val resultHandler = args[3] as ResultHandler<*>
        val executor = invocation.target as Executor
        val cacheKey: CacheKey
        val boundSql: BoundSql
        //由于逻辑关系，只会进入一次
        if (args.size == 4) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameter)
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql)
        } else {
            //6 个参数时
            cacheKey = args[4] as CacheKey
            boundSql = args[5] as BoundSql
        }


        val sql = boundSql.sql

        LOGGER.debug("original sql [$sql]")

        val smt = CCJSqlParserUtil.parse(sql)

        val select = smt as Select

//        SelectUtils
        val selectBody = select.selectBody

        when (selectBody) {
            is PlainSelect -> {
                val fromItem = selectBody.fromItem
                when (fromItem) {
                    is Table -> {
                        val tableName = fromItem.name
                        val alias = fromItem.alias
                        if (multiTenancyConfiguration.needFilterTenant(tableName)) {
                            var where = selectBody.where
                            when (where) {
                                null -> {
                                    var eqto = EqualsTo()
                                    var leftColumn = Column()
                                    leftColumn.columnName = multiTenancyConfiguration.tenantColumnName
                                    alias?.let { leftColumn.table = Table(alias.name) }
                                    eqto.leftExpression=leftColumn
                                    eqto.rightExpression=StringValue("tenant_id")
                                    where=eqto
                                }

                            }
                        }
                    }
                }
            }
        }

        if (selectBody is PlainSelect) {
            selectBody.fromItem
        }






        return executor.query<Any>(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql)

    }

    override fun setProperties(properties: Properties?) {
    }

    override fun plugin(target: Any?): Any {
        return Plugin.wrap(target, this)
    }
}