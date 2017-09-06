package cn.apier.common.cache

import java.util.*


class MemoryCache {

    private val DEFAULT_REGION_NAME = "default"
    private val DEFAULT_EXPIRED_IN_MS = 3600000L
    private val caches: MutableMap<String, HashMap<String, MemoryCacheItem>> = mutableMapOf()

    init {
        caches[DEFAULT_REGION_NAME] = HashMap()
    }

    companion object {
        val default: MemoryCache = MemoryCache()
    }

    fun cache(key: String, value: Any, region: String = DEFAULT_REGION_NAME, expiredInMs: Long = DEFAULT_EXPIRED_IN_MS) {
        val mapItems = this.caches.getOrPut(region) { HashMap() }
        mapItems.put(key, MemoryCacheItem(key, value, System.currentTimeMillis() + expiredInMs))
    }

    fun get(key: String, region: String = DEFAULT_REGION_NAME): Any? {
        val mapItems = this.caches.getOrDefault(region, HashMap())

        Optional.ofNullable(mapItems[key]?.expired).ifPresent {
            if (it) expire(key)
        }

        return mapItems[key]?.value
    }

    private fun removeKey(key: String, region: String = DEFAULT_REGION_NAME) {
        this.caches[region]?.remove(key)
    }

    fun expire(key: String, region: String = DEFAULT_REGION_NAME) {
        this.removeKey(key, region)
    }

    fun expend(key: String, timeToExpendInMs: Long, region: String = DEFAULT_REGION_NAME) {
        this.caches[region]?.get(key)?.expend(timeToExpendInMs)
    }

    fun resetExpiredTime(key: String, expiredInMs: Long, region: String = DEFAULT_REGION_NAME) {
        this.caches[region]?.get(key)?.resetExpireTime(expiredInMs)
    }
}

private class MemoryCacheItem(key: String, value: Any, expiredAt: Long) {
    var value: Any = value
        get() = {
            if (expiredAt < System.currentTimeMillis()) {
                expired = true
                null
            } else value
        }
        private set
    val key: String = key
    var expiredAt: Long = expiredAt
    var expired = false
        private set

    private fun updateExpired() {
        expired = expiredAt < System.currentTimeMillis()
    }

    fun expend(timeToExpendInMs: Long) {
        this.expiredAt = this.expiredAt + timeToExpendInMs
        updateExpired()
    }

    fun resetExpireTime(expiredInMs: Long) {
        this.expiredAt = System.currentTimeMillis() + expiredInMs
    }
}
