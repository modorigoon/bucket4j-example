package me.modorigoon.bucket4jexample

import com.hazelcast.config.CacheSimpleConfig
import com.hazelcast.config.Config
import com.hazelcast.config.RestApiConfig
import com.hazelcast.core.Hazelcast
import io.github.bucket4j.grid.GridBucketState
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.cache.Cache


@Configuration
class HazelcastConfiguration {

    companion object {
        const val CACHE_NAME = "buckets"
    }

    @Bean
    fun bucketCache(): Cache<String, GridBucketState> {
        val config = Config()
        config.isLiteMember = false
        val cacheConfig = CacheSimpleConfig()
        cacheConfig.name = CACHE_NAME
        config.addCacheConfig(cacheConfig)

        val restApiConfig = RestApiConfig()
        restApiConfig.isEnabled = true
        config.networkConfig.restApiConfig = restApiConfig

        val hazelcastInstance = Hazelcast.newHazelcastInstance(config)
        val cacheManager = hazelcastInstance.cacheManager

        return cacheManager.getCache(CACHE_NAME)
    }
}
