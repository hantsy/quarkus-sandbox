package com.example

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import java.util.*


class WireMockExtensions() : QuarkusTestResourceLifecycleManager {
    private var wireMockServer: WireMockServer? = null
    override fun start(): Map<String, String> {
        wireMockServer = WireMockServer()
        wireMockServer!!.start()
        wireMockServer!!.stubFor(
            WireMock.get(WireMock.urlEqualTo("/extensions?id=io.quarkus:quarkus-rest-client"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            "[{" +
                                    "\"id\": \"io.quarkus:quarkus-rest-client\"," +
                                    "\"name\": \"REST Client Classic\"" +
                                    "}]"
                        )
                )
        )
        wireMockServer!!.stubFor(
            WireMock.get(WireMock.urlMatching(".*")).atPriority(10)
                .willReturn(WireMock.aResponse().proxiedFrom("https://stage.code.quarkus.io/api"))
        )
        return Collections.singletonMap(
            "quarkus.rest-client.\"org.acme.rest.client.ExtensionsService\".url",
            wireMockServer!!.baseUrl()
        )
    }

    override fun stop() {
        if (null != wireMockServer) {
            wireMockServer!!.stop()
        }
    }
}