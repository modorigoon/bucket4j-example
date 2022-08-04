package me.modorigoon.bucket4jexample

import io.github.bucket4j.grid.GridBucketState
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.cache.Cache


@Configuration
class WebMvcConfiguration(private val bucketCache: Cache<String, GridBucketState>) {

    @Bean
    fun filterRegistration(): FilterRegistrationBean<RateLimitFilter> {
        val registration: FilterRegistrationBean<RateLimitFilter> = FilterRegistrationBean()
        registration.filter = rateLimitFilter()
        registration.addUrlPatterns("/greet")
        return registration
    }

    @Bean
    fun rateLimitFilter(): RateLimitFilter {
        return RateLimitFilter(bucketCache)
    }
}
