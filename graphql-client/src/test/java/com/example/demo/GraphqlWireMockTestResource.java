package com.example.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.github.nilwurtz.GraphqlBodyMatcher;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * Starts a WireMock server with the GraphQL body matcher registered and points every config key listed in
 * {@code mockedApiBaseUrlConfigKeys} at its {@code /graphql} endpoint, so the GraphQL clients under test
 * talk to the mock instead of a running server.
 * <p>
 * Every client key goes through this one server: when the typesafe and dynamic test classes each declared
 * the resource with only their own key, the request went to a server whose stubs had been registered on the
 * other instance and WireMock answered 404.
 */
public class GraphqlWireMockTestResource implements QuarkusTestResourceLifecycleManager {
    private static final Logger LOGGER = Logger.getLogger(GraphqlWireMockTestResource.class.getName());

    public static final String MOCKED_API_BASE_URL_CONFIG_KEYS = "mockedApiBaseUrlConfigKeys";
    public static final String GRAPHQL_PATH = "/graphql";

    private Map<String, String> initArgs;
    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(options().dynamicPort().extensions(new GraphqlBodyMatcher()));
        wireMockServer.start();
        String url = wireMockServer.url(GRAPHQL_PATH);
        LOGGER.log(Level.INFO, "WireMock GraphQL endpoint: {0}", url);
        return Arrays.stream(this.initArgs.get(MOCKED_API_BASE_URL_CONFIG_KEYS).split(","))
                .map(String::trim)
                .filter(key -> !key.isEmpty())
                .collect(Collectors.toMap(key -> key, key -> url));
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
        if (!this.initArgs.containsKey(MOCKED_API_BASE_URL_CONFIG_KEYS)) {
            throw new IllegalArgumentException("The init arg [" + MOCKED_API_BASE_URL_CONFIG_KEYS + "] is required.");
        }
        LOGGER.log(Level.INFO, "init args from test class: {0}", initArgs);
    }
}
