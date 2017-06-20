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
interface CountryEntryRepository : JpaRepository<CountryEntry, String>

@Repository
interface ProvinceEntryRepository : JpaRepository<ProvinceEntry, String>


@Repository
interface CityEntryRepository : JpaRepository<CityEntry, String>


@Repository
interface DistrictEntryRepository : JpaRepository<DistrictEntry, String>
