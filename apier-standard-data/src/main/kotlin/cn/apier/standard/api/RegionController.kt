package cn.apier.standard.api

import cn.apier.common.api.Result
import cn.apier.common.util.ExecuteTool
import cn.apier.standard.application.service.RegionService
import cn.apier.standard.query.entry.CountryEntry
import cn.apier.standard.query.service.RegionQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by yanjunhua on 2017/6/6.
 */

@RestController
@RequestMapping("/region")
class RegionController(@Autowired
                       val regionService: RegionService, @Autowired val regionQueryService: RegionQueryService) {
    @GetMapping("/countries")
    fun listAllCountry(): Result<List<CountryEntry>> = ExecuteTool.executeQueryWithTry { this.regionQueryService.listAllCountry() }

    @PostMapping("/country/new")
    fun addNewCountry(name: String,code:String, description: String): Result<Any> = ExecuteTool.executeWithTry {
        this.regionService.newCountry(name,code, description)
    }

    @PostMapping("/country/update")
    fun updateCountry(uid: String, name: String,code:String, description: String?): Result<Any> =
            ExecuteTool.executeWithTry { this.regionService.updateCountry(uid, name,code, description) }


    @PostMapping("/province/new")
    fun addProvince(countryId: String, name: String,code:String, description: String): Result<Any> = ExecuteTool.executeWithTry {
        this.regionService.newProvince(countryId, name,code, description)
    }

    @PostMapping("/province/update")
    fun updateProvince(uid: String, countryId: String, name: String,code:String, description: String): Result<Any> = ExecuteTool.executeWithTry {
        this.regionService.updateProvince(uid, countryId, name,code, description)
    }

    @PostMapping("/city/new")
    fun addCity(provinceId: String, name: String,code:String, description: String): Result<Any> = ExecuteTool.executeWithTry { this.regionService.newCity(provinceId, name,code, description) }


    @PostMapping("/city/update")
    fun updateCity(uid: String, provinceId: String, name: String,code:String, description: String): Result<Any> = ExecuteTool.executeWithTry { this.regionService.updateCity(uid, provinceId, name,code, description) }

    @PostMapping("/district/new")
    fun addDistrict(cityId: String, name: String,code:String, description: String?): Result<Any> = ExecuteTool.executeWithTry { this.regionService.newDistrict(cityId, name,code, description) }

    @PostMapping("/district/update")
    fun updateDistrict(uid: String, cityId: String, name: String,code:String, description: String?): Result<Any> = ExecuteTool.executeWithTry { this.regionService.updateCity(uid, cityId, name,code, description) }

    @PostMapping("/init")
    fun init() {
        this.regionService.importData("/Users/yanjunhua/tmp/region-2017.txt")
    }

}