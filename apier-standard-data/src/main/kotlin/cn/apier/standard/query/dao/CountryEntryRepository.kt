package cn.apier.standard.query.dao

import cn.apier.standard.query.entry.CountryEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Created by yanjunhua on 2017/6/14.
 */
@Repository
interface CountryEntryRepository : JpaRepository<CountryEntry, String> {
}