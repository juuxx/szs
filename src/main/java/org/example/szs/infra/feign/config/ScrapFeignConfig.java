package org.example.szs.infra.feign.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ScrapFeignConfig implements RequestInterceptor {

	private final ScrapApiProperties properties;

	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header("X-API-KEY", properties.getApiKey());
	}
}
