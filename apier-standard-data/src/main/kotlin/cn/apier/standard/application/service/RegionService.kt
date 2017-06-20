package cn.apier.standard.application.service

import cn.apier.common.util.DateTimeUtil
import cn.apier.common.util.ExecuteTool
import cn.apier.common.util.UUIDUtil
import cn.apier.standard.application.command.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by yanjunhua on 2017/6/12.
 */
@Service
class RegionService {

    @Autowired
    lateinit var commandGateway: CommandGateway


    fun newCountry(name: String, description: String?) {

        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")

        commandGateway.sendAndWait<Unit>(CreateCountryCommand(UUIDUtil.commonUUID(), name, DateTimeUtil.now(), description))
    }

    fun updateCountry(uid: String, name: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")

        commandGateway.sendAndWait<Unit>(UpdateCountryCommand(uid, name, description))
    }


    fun newProvince(countryId: String, name: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(countryId, "countryId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(CreateProvinceCommand(UUIDUtil.commonUUID(), countryId, name, description))
    }


    fun updateProvince(uid: String, countryId: String, name: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        ExecuteTool.checkStringParameterNonNullOrEmpty(countryId, "countryId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(updateProvince(uid, countryId, name, description))
    }

    fun newCity(provinceId: String, name: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(provinceId, "provinceId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(CreateCityCommand(UUIDUtil.commonUUID(), provinceId, name, description))
    }

    fun updateCity(uid: String, provinceId: String, name: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        ExecuteTool.checkStringParameterNonNullOrEmpty(provinceId, "provinceId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(UpdateCityCommand(uid, provinceId, name, description))
    }

    fun newDistrict(cityId: String, name: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(cityId, "cityId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(CreateDistrictCommand(UUIDUtil.commonUUID(), cityId, name, description))
    }

    fun updateDistrict(uid: String, cityId: String, name: String, description: String?) {
        ExecuteTool.checkStringParameterNonNullOrEmpty(uid, "uid")
        ExecuteTool.checkStringParameterNonNullOrEmpty(cityId, "cityId")
        ExecuteTool.checkStringParameterNonNullOrEmpty(name, "name")
        commandGateway.sendAndWait<Unit>(UpdateDistrictCommand(uid, cityId, name, description))
    }
}