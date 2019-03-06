package com.alex.auth;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(JacksonJsonProvider.class);
        register(OpenApiResource.class);
        register(ThrowablesMapper.class);
        register(AuthServiceImpl.class);
    }
}
