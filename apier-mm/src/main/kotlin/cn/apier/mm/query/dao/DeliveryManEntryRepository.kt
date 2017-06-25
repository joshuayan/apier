package cn.apier.mm.query.dao

import cn.apier.mm.query.entry.DeliveryManEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Created by yanjunhua on 2017/6/22.
 */
@Repository
interface DeliveryManEntryRepository : JpaRepository<DeliveryManEntry, String>