package cn.apier.standard.query.service

import cn.apier.standard.query.dao.CountryEntryRepository
import cn.apier.standard.query.entry.CountryEntry
import cn.apier.standard.query.entry.ProvinceEntry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by yanjunhua on 2017/6/12.
 */
@Service
class RegionQueryService(@Autowired val countryEntryRepository: CountryEntryRepository) {

    fun listAllCountry(): List<CountryEntry> {
        return this.countryEntryRepository.findAll()
    }

    fun queryProvinceByCountry(): List<ProvinceEntry> {
        return emptyList()
    }
}