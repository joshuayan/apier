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
        val mapItems = this.caches.getOrDefault(key, HashMap())

        Optional.ofNullable(mapItems[key]?.expired).ifPresent {
            if (it) removeKey(key)
        }

        return mapItems[key]?.value
    }

    fun removeKey(key: String) {
        this.caches.remove(key)
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
    val expiredAt: Long = expiredAt
    var expired = false
        private set
}
