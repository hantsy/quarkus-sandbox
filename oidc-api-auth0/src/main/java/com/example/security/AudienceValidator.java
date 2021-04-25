package com.example.security;

import io.quarkus.oidc.OidcConfigurationMetadata;
import io.quarkus.security.identity.SecurityIdentity;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

//@Provider
public class AudienceValidator implements ContainerRequestFilter {

    @Inject
    OidcConfigurationMetadata configMetadata;

    @Inject
    JsonWebToken jwt;

    @Inject
    SecurityIdentity identity;

    public void filter(ContainerRequestContext requestContext) {
        String aud = configMetadata.get("audience");//.replace("{tenant-id}", identity.getAttribute("tenant-id"));
        if (!jwt.getAudience().contains(aud)) {
            requestContext.abortWith(Response.status(401).build());
        }
    }
}