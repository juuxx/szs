package org.example.szs.infra.feign.client;

import org.example.szs.infra.feign.config.ScrapFeignConfig;
import org.example.szs.infra.feign.dto.ScrapApiResponse;
import org.example.szs.infra.feign.dto.ScrapRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
	name = "scrapClient",
	url = "${external.scrap.base-url}",
	configuration = ScrapFeignConfig.class
)
public interface ScrapClient {
	@PostMapping(value = "/scrap", consumes = "application/json")
	ScrapApiResponse scrap(@RequestBody ScrapRequest request);
}