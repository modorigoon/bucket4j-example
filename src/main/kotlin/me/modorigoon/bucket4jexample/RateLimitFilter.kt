package me.modorigoon.bucket4jexample

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.grid.GridBucketState
import io.github.bucket4j.grid.ProxyManager
import io.github.bucket4j.grid.jcache.JCache
import org.springframework.http.HttpStatus
import org.springframework.web.filter.GenericFilterBean
import java.time.Duration
import java.util.function.Supplier
import javax.cache.Cache
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class RateLimitFilter(
    private val cache: Cache<String, GridBucketState>,
    private val buckets: ProxyManager<String>? = Bucket4j.extension(JCache::class.java).proxyManagerForCache(cache)
) : GenericFilterBean() {

    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?, p2: FilterChain?) {
        val request = p0 as HttpServletRequest
        val response = p1 as HttpServletResponse

        val bucketConfiguration = Bucket4j.configurationBuilder()
            .addLimit(Bandwidth.simple(10, Duration.ofMinutes(1))).build()

        val configurationSupplier: Supplier<BucketConfiguration> = Supplier<BucketConfiguration> { bucketConfiguration }
        val bucket = buckets!!.getProxy(request.remoteAddr, configurationSupplier)
        val probe = bucket.tryConsumeAndReturnRemaining(1)

        if (probe.isConsumed) {
            response.setHeader("X-Rate-Limit-Remaining", probe.remainingTokens.toString())
            p2?.doFilter(request, response)
        } else {
            response.contentType = "text/plain"
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            response.writer.append(HttpStatus.TOO_MANY_REQUESTS.reasonPhrase)
        }
    }
}
