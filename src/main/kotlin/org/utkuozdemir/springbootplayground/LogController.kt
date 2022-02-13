package org.utkuozdemir.springbootplayground

import io.fabric8.kubernetes.client.DefaultKubernetesClient
import io.fabric8.kubernetes.client.KubernetesClient
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.Duration
import java.util.*

private val logger = KotlinLogging.logger {}

private const val NAMESPACE = "ccp-mgmt-ingress"

@RestController
class LogController {

    private val kubernetesClient: KubernetesClient = DefaultKubernetesClient()

    @GetMapping(path = ["/logs"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun logs(): Flux<String> {
        logger.info { "Requested Logs" }
        val pod = kubernetesClient
                .pods()
                .inNamespace(NAMESPACE)
                .withLabel("component", "controller")
                .list()
                .items[0]

        val logWatch = kubernetesClient
                .pods()
                .inNamespace(NAMESPACE)
                .withName(pod.metadata.name)
                .tailingLines(20)
                .watchLog()

        logger.info { "Watching log ${pod.metadata.name}" }

        val isr = InputStreamReader(logWatch.output, Charsets.UTF_8)
        val br = BufferedReader(isr)
        return Flux.fromStream(br.lines())
                .doOnNext { logger.info { it } }
                .doFinally { signalType ->
                    logger.info { "Signal type: $signalType" }
                    br.close()
                    logWatch.close()
                    logger.info { "Closed LogWatch and the reader" }
                }
    }

    @GetMapping(path = ["/test-logs"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun testLogs(): Flux<String> {
        logger.info { "Requested Test Logs" }
        return Flux.interval(Duration.ofSeconds(1))
                .map { "Test UUID: " + UUID.randomUUID().toString() }
                .doOnNext { logger.info { it } }
                .doFinally { logger.info { "Signal type: $it" } }
    }
}
