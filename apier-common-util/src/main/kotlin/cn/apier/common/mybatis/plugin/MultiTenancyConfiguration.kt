package cn.apier.common.mybatis.plugin

class MultiTenancyConfiguration {

    var tenantColumnName: String = "tenant_id"
    var tablesNameWithTenant: MutableList<String> = mutableListOf()

    fun needFilterTenant(tableName: String) = this.tablesNameWithTenant.contains(tableName)
}