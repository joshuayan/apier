package cn.apier.standard.query.listener

import cn.apier.common.exception.CommonException
import cn.apier.common.util.ExecuteTool
import cn.apier.standard.domain.event.*
import cn.apier.standard.query.dao.CityEntryRepository
import cn.apier.standard.query.dao.CountryEntryRepository
import cn.apier.standard.query.dao.DistrictEntryRepository
import cn.apier.standard.query.dao.ProvinceEntryRepository
import cn.apier.standard.query.entry.CityEntry
import cn.apier.standard.query.entry.CountryEntry
import cn.apier.standard.query.entry.DistrictEntry
import cn.apier.standard.query.entry.ProvinceEntry
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * Created by yanjunhua on 2017/6/15.
 */

@Component
class CountryListener {

    @Autowired
    lateinit var countryEntryRepository: CountryEntryRepository

    @EventHandler
    fun onCreation(createdEvent: CountryCreatedEvent) {
        Mono.just<CountryEntry>(CountryEntry(createdEvent.id, createdEvent.name, createdEvent.code, createdEvent.createdAt, createdEvent.description))
                .subscribe { this.countryEntryRepository.save(it) }
    }


    @EventHandler
    fun onUpdated(updatedEvent: CountryUpdatedEvent) {

        Mono.just<CountryEntry>(this.countryEntryRepository.getOne(updatedEvent.id)).map { it.name = updatedEvent.name;it.description = updatedEvent.description;it }
                .subscribe { this.countryEntryRepository.save(it) }
    }
}

@Component
class ProvinceListener {
    @Autowired
    lateinit var provinceEntryRepository: ProvinceEntryRepository

    @EventHandler
    fun onCreation(provinceCreatedEvent: ProvinceCreatedEvent) {
        Mono.just(ProvinceEntry(provinceCreatedEvent.uid, provinceCreatedEvent.countryId, provinceCreatedEvent.name, provinceCreatedEvent.code, provinceCreatedEvent.createdAt, provinceCreatedEvent.description))
                .subscribe { this.provinceEntryRepository.save(it) }
    }

    @EventHandler
    fun onUpdated(provinceUpdatedEvent: ProvinceUpdatedEvent) {
        Mono.just<ProvinceEntry>(this.provinceEntryRepository.getOne(provinceUpdatedEvent.uid))
                .map {
                    it.countryId = provinceUpdatedEvent.countryId;it.description = provinceUpdatedEvent.description
                    it.name = provinceUpdatedEvent.name;it
                }.subscribe { this.provinceEntryRepository.save(it) }
    }
}

@Component
class CityListener {
    @Autowired
    lateinit var cityEntryRepository: CityEntryRepository

    @EventHandler
    fun onCreation(cityCreatedEvent: CityCreatedEvent) {
        Mono.just<CityEntry>(CityEntry(cityCreatedEvent.uid, cityCreatedEvent.provinceId, cityCreatedEvent.name, cityCreatedEvent.code, cityCreatedEvent.createdAt, cityCreatedEvent.description))
                .subscribe { this.cityEntryRepository.save(it) }
    }

    @EventHandler
    fun onUpdated(cityUpdatedEvent: CityUpdatedEvent) {
        ExecuteTool.loadAndThen { this.cityEntryRepository.getOne(cityUpdatedEvent.uid) }.ifNonNull().invalidOperationException()
                .then { it.name = cityUpdatedEvent.name;it.description = cityUpdatedEvent.description;it.provinceId = cityUpdatedEvent.provinceId }
                .then { this.cityEntryRepository.save(it) }
    }
}

@Component
class DistrictListener {

    @Autowired
    lateinit var districtEntryRepository: DistrictEntryRepository

    @EventHandler
    fun onCreation(districtCreatedEvent: DistrictCreatedEvent) {
        Mono.just(DistrictEntry(districtCreatedEvent.uid, districtCreatedEvent.cityId, districtCreatedEvent.name, districtCreatedEvent.code, districtCreatedEvent.createdAt, districtCreatedEvent.description))
                .subscribe { this.districtEntryRepository.save(it) }
    }

    @EventHandler
    fun onUpdated(districtUpdatedEvent: DistrictUpdatedEvent) {
         this.districtEntryRepository.findById(districtUpdatedEvent.uid) .orElseThrow{CommonException.invalidOperation()}
                .also { it.cityId = districtUpdatedEvent.cityId;it.description = districtUpdatedEvent.description;it.name = districtUpdatedEvent.name }
                .let { this.districtEntryRepository.save(it) }
    }
}