package com.example

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import mockwebserver3.MockWebServer
import java.util.logging.Logger

/**
 * Starts OkHttp's MockWebServer on an ephemeral port before Quarkus boots and publishes its
 * address under the config key given by the [MOCKED_API_BASE_URL_CONFIG_KEY] init arg, so the
 * MicroProfile Rest Client under test dials the mock instead of the peer on 8080.
 *
 * MockWebServer is a queue, not a router: it answers requests in the order the test enqueued
 * them and records every call, which is why each test enqueues exactly what it sends and then
 * asserts on [MockWebServer.takeRequest].
 */
class MockWebServerTestResource : QuarkusTestResourceLifecycleManager {
    companion object {
        const val MOCKED_API_BASE_URL_CONFIG_KEY = "mockedApiBaseUrlConfigKey"

        private val LOGGER: Logger = Logger.getLogger(MockWebServerTestResource::class.java.name)
    }

    private lateinit var configKey: String
    private var mockWebServer: MockWebServer? = null

    override fun init(initArgs: Map<String, String>) {
        configKey = initArgs[MOCKED_API_BASE_URL_CONFIG_KEY]
            ?: throw IllegalArgumentException("The init arg [$MOCKED_API_BASE_URL_CONFIG_KEY] is required.")
        LOGGER.info("init args from test class: $initArgs")
    }

    override fun start(): Map<String, String> {
        val server = MockWebServer()
        server.start()
        mockWebServer = server
        // No trailing slash: the resource paths in the client interface already start with "/".
        val baseUrl = "http://${server.hostName}:${server.port}"
        return mapOf(configKey to baseUrl)
    }

    override fun inject(testInjector: QuarkusTestResourceLifecycleManager.TestInjector) {
        testInjector.injectIntoFields(
            mockWebServer,
            QuarkusTestResourceLifecycleManager.TestInjector.AnnotatedAndMatchesType(
                InjectMockWebServer::class.java,
                MockWebServer::class.java
            )
        )
    }

    override fun stop() {
        mockWebServer?.close()
        mockWebServer = null
    }
}
