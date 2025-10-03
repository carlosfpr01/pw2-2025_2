package dev.ifrs.health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;



@Readiness

public class Ready implements HealthCheck{
    @ConfigProperty(name = "ifrs.key")
    String key;

    @Override
    public HealthCheckResponse call(){

        HealthCheckResponseBuilder response = HealthCheckResponse.named("ready");
        response.up().withData(key, "value");
        return response.build();

    }

}
