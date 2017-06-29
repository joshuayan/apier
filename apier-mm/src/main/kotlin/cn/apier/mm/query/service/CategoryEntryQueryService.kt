package cn.apier.mm.query.service

import cn.apier.mm.query.dao.CategoryEntryRepository
import cn.apier.mm.query.entry.CategoryEntry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by yanjunhua on 2017/6/28.
 */
@Service
class CategoryEntryQueryService {
    @Autowired
    private lateinit var categoryEntryRepository: CategoryEntryRepository

    fun checkIfDuplicatedName(name: String, tenantId: String): Boolean = this.categoryEntryRepository.exists(Example.of(CategoryEntry()
            .also { it.name = name }, ExampleMatcher.matching()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.exact())
            .withMatcher("tenantId", ExampleMatcher.GenericPropertyMatchers.exact())
    ))

    fun checkIfDuplicatedNameExcludeId(name: String, tenantId: String, excludeId: String): Boolean = Optional.ofNullable(this.categoryEntryRepository.findByNameAndTenantIdAndUidNot(name, tenantId, excludeId)).isPresent
}