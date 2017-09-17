package cn.apier.common.cache

import java.util.*


class MemoryCache {

    private val DEFAULT_REGION_NAME = "default"
    private val DEFAULT_EXPIRED_IN_MS = 3600000L
    private val caches: MutableMap<String, HashMap<String, MemoryCacheItem<Any>>> = mutableMapOf()

    init {
        caches[DEFAULT_REGION_NAME] = HashMap()
    }

    companion object {
        val default: MemoryCache = MemoryCache()
    }

    fun <T : Any> cache(key: String, value: T, region: String = DEFAULT_REGION_NAME, expiredInMs: Long = DEFAULT_EXPIRED_IN_MS) {
        val mapItems = this.caches.getOrPut(region) { HashMap<String, MemoryCacheItem<Any>>() }
        mapItems.put(key, MemoryCacheItem(key, value, System.currentTimeMillis() + expiredInMs))
    }

    fun get(key: String, region: String = DEFAULT_REGION_NAME): Any? {
        val mapItems = this.caches.getOrDefault(region, HashMap())

        Optional.ofNullable(mapItems[key]?.expired).ifPresent {
            if (it) expire(key)
        }

        return mapItems[key]?.getValue()
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

private class MemoryCacheItem<T>(key: String, value: T, expiredAt: Long) {
    private val mValue: T = value
    val key: String = key
    var expiredAt: Long = expiredAt
    var expired = false
        private set

    private fun updateExpired() {
        expired = expiredAt < System.currentTimeMillis()
    }

    fun getValue(): T? {
        var result: T? = this.mValue
        if (expiredAt < System.currentTimeMillis()) {
            expired = true
            result = null
        }
        return result
    }

    fun expend(timeToExpendInMs: Long) {
        this.expiredAt = this.expiredAt + timeToExpendInMs
        updateExpired()
    }

    fun resetExpireTime(expiredInMs: Long) {
        this.expiredAt = System.currentTimeMillis() + expiredInMs
    }
}
