package com.example.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WireMockTestResource implements QuarkusTestResourceLifecycleManager {
    private static final Logger LOGGER = Logger.getLogger(WireMockTestResource.class.getName());

    public static final String MOCKED_API_BASE_URL_CONFIG_KEY = "mockedApiBaseUrlConfigKey";

    private Map<String, String> initArgs;
    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        return Map.of(
                this.initArgs.get(MOCKED_API_BASE_URL_CONFIG_KEY),
                wireMockServer.baseUrl()
        );
    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(wireMockServer,
                new TestInjector.AnnotatedAndMatchesType(InjectWireMock.class, WireMockServer.class));
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }

    @Override
    public void init(Map<String, String> initArgs) {
        this.initArgs = initArgs;
        if (!this.initArgs.containsKey(MOCKED_API_BASE_URL_CONFIG_KEY)) {
            throw new IllegalArgumentException("The init arg [" + MOCKED_API_BASE_URL_CONFIG_KEY + "] is required.");
        }
        LOGGER.log(Level.INFO, "init args from test class: {0}", initArgs);
    }
}
