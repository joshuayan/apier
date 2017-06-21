package cn.apier.standard.query.dao

import cn.apier.standard.query.entry.CityEntry
import cn.apier.standard.query.entry.CountryEntry
import cn.apier.standard.query.entry.DistrictEntry
import cn.apier.standard.query.entry.ProvinceEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Created by yanjunhua on 2017/6/14.
 */
@Repository
interface CountryEntryRepository : JpaRepository<CountryEntry, String> {
    fun findByName(name: String): CountryEntry?
    fun findByCode(code: String): CountryEntry?
}

@Repository
interface ProvinceEntryRepository : JpaRepository<ProvinceEntry, String> {
    fun findByName(name: String): ProvinceEntry?
    fun findByCode(code: String): ProvinceEntry?
}


@Repository
interface CityEntryRepository : JpaRepository<CityEntry, String> {
    fun findByName(name: String): CityEntry?
    fun findByCode(code: String): CityEntry?
}


@Repository
interface DistrictEntryRepository : JpaRepository<DistrictEntry, String> {
    fun findByName(name: String): DistrictEntry?
    fun findByCode(code: String): DistrictEntry?
}
