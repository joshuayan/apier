package cn.apier.standard.query.dao

import cn.apier.standard.query.entry.CountryEntry
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

/**
 * Created by yanjunhua on 2017/6/24.
 */

@Mapper
interface CountryEntryMapper {
    @Select("select * from country_entry")
    fun findAll(): List<CountryEntry>
}