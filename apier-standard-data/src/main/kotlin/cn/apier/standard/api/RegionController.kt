package cn.apier.standard.api

import cn.apier.common.api.Result
import cn.apier.common.util.ExecuteTool
import cn.apier.standard.application.service.RegionService
import cn.apier.standard.domain.model.Country
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
    fun addNewCountry(name: String, description: String): Result<Any> = ExecuteTool.executeWithTry {
        this.regionService.addNewCountry(name, description)
    }

    @PostMapping("/country/update")
    fun updateCountry(id: String, name: String, description: String?): Result<Any> =
            ExecuteTool.executeWithTry { this.regionService.updateCountry(id, name, description) }
}