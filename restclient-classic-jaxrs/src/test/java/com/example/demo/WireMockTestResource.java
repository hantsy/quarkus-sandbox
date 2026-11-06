package com.example.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * Starts a WireMock server on a random port before Quarkus boots and publishes its
 * address under the config key given by the {@code mockedApiBaseUrlConfigKey} init arg,
 * so the test profile of the module points its client at the mock instead of at 8080.
 * <p>
 * Unlike the Quarkiverse WireMock dev service, which runs a shaded standalone server and
 * can only be reached through {@code quarkus.rest-client.<configKey>.url}, this resource
 * drives an in-process {@link WireMockServer} built from the unshaded API. That keeps the
 * door open for server-side extensions and for clients configured through arbitrary
 * properties, as {@code post-service.base_url} is here.
 */
public class WireMockTestResource implements QuarkusTestResourceLifecycleManager {
    private static final Logger LOGGER = Logger.getLogger(WireMockTestResource.class.getName());

    public static final String MOCKED_API_BASE_URL_CONFIG_KEY = "mockedApiBaseUrlConfigKey";

    private Map<String, String> initArgs;
    private WireMockServer wireMockServer;

    @Override
    public void init(Map<String, String> initArgs) {
        this.initArgs = initArgs;
        if (!this.initArgs.containsKey(MOCKED_API_BASE_URL_CONFIG_KEY)) {
            throw new IllegalArgumentException("The init arg [" + MOCKED_API_BASE_URL_CONFIG_KEY + "] is required.");
        }
        LOGGER.log(Level.INFO, "init args from test class: {0}", initArgs);
    }

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(options().dynamicPort());
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
}
