package cn.apier.standard.query.service

import cn.apier.standard.query.dao.*
import cn.apier.standard.query.entry.CityEntry
import cn.apier.standard.query.entry.CountryEntry
import cn.apier.standard.query.entry.DistrictEntry
import cn.apier.standard.query.entry.ProvinceEntry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by yanjunhua on 2017/6/12.
 */
@Service
open class RegionQueryService(@Autowired val countryEntryRepository: CountryEntryRepository, @Autowired val provinceEntryRepository: ProvinceEntryRepository, @Autowired val cityEntryRepository: CityEntryRepository, @Autowired val districtEntryRepository: DistrictEntryRepository) {

    @Autowired
    lateinit var countryEntryMapper: CountryEntryMapper

    fun listAllCountry(): List<CountryEntry> {
        return this.countryEntryMapper.findAll()
    }

    fun queryProvinceByCountry(): List<ProvinceEntry> {
        return emptyList()
    }

    fun queryCountryByName(name: String): CountryEntry? = this.countryEntryRepository.findByName(name)
    fun queryCountryByCode(code: String): CountryEntry? = this.countryEntryRepository.findByCode(code)

    fun queryProvinceByName(name: String): ProvinceEntry? = this.provinceEntryRepository.findByName(name)
    fun queryProvinceByCode(code: String): ProvinceEntry? = this.provinceEntryRepository.findByCode(code)

    fun queryCityByName(name: String): CityEntry? = this.cityEntryRepository.findByName(name)
    fun queryCityByCode(code: String): CityEntry? = this.cityEntryRepository.findByCode(code)

    fun queryDistrictByName(name: String): DistrictEntry? = this.districtEntryRepository.findByName(name)
    fun queryDistrictByCode(code: String): DistrictEntry? = this.districtEntryRepository.findByCode(code)

}