package org.example.szs.infra.feign.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Configuration
@ConfigurationProperties(prefix = "external.scrap")
@Getter
public class ScrapApiProperties {
	private String baseUrl;
	private String apiKey;
}