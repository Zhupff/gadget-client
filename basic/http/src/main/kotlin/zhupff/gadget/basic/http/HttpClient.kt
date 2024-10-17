package zhupff.gadget.basic.http

import okhttp3.OkHttpClient
import okhttp3.Protocol
import zhupff.gadgets.logger.logI
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

object HttpClient : AtomicReference<OkHttpClient>() {

    fun initClient() {
        logI("initClient start.")
        val builder = OkHttpClient.Builder()
            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
            .readTimeout(10000L, TimeUnit.MILLISECONDS)
            .writeTimeout(10000L, TimeUnit.MILLISECONDS)
            .protocols(listOf(Protocol.HTTP_1_1))
        val client = builder.build()
        client.dispatcher.maxRequests = 5
        client.dispatcher.maxRequestsPerHost = 3

        val reInit = getAndSet(client) != null
        logI("initClient end. reInit=${reInit}")
    }
}