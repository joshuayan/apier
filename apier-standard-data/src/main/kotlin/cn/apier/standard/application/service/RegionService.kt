package cn.apier.standard.application.service

import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.ExecuteTool
import cn.apier.common.util.UUIDUtil
import cn.apier.standard.application.command.*
import cn.apier.standard.query.entry.CityEntry
import cn.apier.standard.query.entry.CountryEntry
import cn.apier.standard.query.entry.ProvinceEntry
import cn.apier.standard.query.service.RegionQueryService
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.File
import java.util.*
import java.util.stream.Collectors
import javax.transaction.Transactional

/**
 * Created by yanjunhua on 2017/6/12.
 */
@Service
open class RegionService(@Autowired open var regionQueryService: RegionQueryService) {

    @Autowired
    lateinit open var commandGateway: CommandGateway


    fun newCountry(name: String, code: String, description: String?) {

        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")

        commandGateway.sendAndWait<Unit>(CreateCountryCommand(UUIDUtil.commonUUID(), name, code, DateTimeUtil.now(), description))
    }

    fun updateCountry(uid: String, name: String, code: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")

        commandGateway.sendAndWait<Unit>(UpdateCountryCommand(uid, name, code, description))
    }


    fun newProvince(countryId: String, name: String, code: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(countryId, "countryId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(CreateProvinceCommand(UUIDUtil.commonUUID(), countryId, name, code, description))
    }


    fun updateProvince(uid: String, countryId: String, name: String, code: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        ExecuteTool.checkStringParameterNonNullOrEmpty(countryId, "countryId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(UpdateProvinceCommand(uid, countryId, name, code, description))
    }

    fun newCity(provinceId: String, name: String, code: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(provinceId, "provinceId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(CreateCityCommand(UUIDUtil.commonUUID(), provinceId, name, code, description))
    }

    fun updateCity(uid: String, provinceId: String, name: String, code: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        ExecuteTool.checkStringParameterNonNullOrEmpty(provinceId, "provinceId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(UpdateCityCommand(uid, provinceId, name, code, description))
    }

    fun newDistrict(cityId: String, name: String, code: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(cityId, "cityId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(CreateDistrictCommand(UUIDUtil.commonUUID(), cityId, name, code, description))
    }

    fun updateDistrict(uid: String, cityId: String, name: String, code: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        ExecuteTool.checkStringParameterNonNullOrEmpty(cityId, "cityId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(UpdateDistrictCommand(uid, cityId, name, code, description))
    }

    @Transactional
    fun importData(filePath: String) {

        var country: CountryEntry? = this.regionQueryService.queryCountryByName("中国")

        when (country) {
            null -> {
                this.newCountry("中国", "100000", "cn")
            }
        }

        country = this.regionQueryService.queryCountryByName("中国")

        val file = File(filePath)
        file.bufferedReader().lines().map<Pair<String, String>> { val items = it.apply { it.trim() }.split(" +".toRegex());Pair<String, String>(items[0].trim(), items[1].trim()) }
                .collect(Collectors.groupingBy<Pair<String, String>, String> { it.first.substring(0..1) })
                .toList().forEach {
            var currentProvince: ProvinceEntry? = null
            var currentCity: CityEntry? = null
            it.second.forEach {
                println("${it.first}-${it.second}");
                when {
                    it.first.matches("""^\d{2}0000$""".toRegex()) -> {
                        println("province:${it.second}")
                        currentProvince = Optional.ofNullable(this.regionQueryService.queryProvinceByCode(it.first)).orElseGet { this.newProvince(country!!.uid, it.second, it.first, it.second);this.regionQueryService.queryProvinceByCode(it.first) }
                    }
                    it.first.matches("""^\d{4}00$""".toRegex()) -> {
                        println("city:${it.second}")
                        currentCity = Optional.ofNullable(this.regionQueryService.queryCityByCode(it.first)).orElseGet { this.newCity(currentProvince!!.uid, it.second, it.first, it.second);this.regionQueryService.queryCityByCode(it.first) }
                    }
                    else -> {
                        println("district:${it.second}")
                        this.newDistrict(currentCity!!.uid, it.second, it.first, it.second)
                    }
                }
            }

        }

        println("import data DONE")
    }

}