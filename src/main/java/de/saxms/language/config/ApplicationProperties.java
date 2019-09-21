package de.saxms.language.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Local Server.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

}
