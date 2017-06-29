package cn.apier.mm.query.dao

import cn.apier.mm.query.entry.CategoryEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Created by yanjunhua on 2017/6/21.
 */

@Repository
interface CategoryEntryRepository : JpaRepository<CategoryEntry, String> {
    fun findByNameAndTenantId(name: String, tenantId: String): CategoryEntry
    fun findByNameAndTenantIdAndUidNot(name: String, tenantId: String, uid: String): CategoryEntry
}