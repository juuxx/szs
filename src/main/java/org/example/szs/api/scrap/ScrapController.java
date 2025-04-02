package org.example.szs.api.scrap;

import org.example.szs.domain.scrap.ScrapService;
import org.example.szs.infra.auth.CurrentUser;
import org.example.szs.infra.auth.LoginUser;
import org.example.szs.infra.feign.dto.ScrapApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ScrapController {

	private final ScrapService scrapService;

	@PostMapping("/szs/scrap")
	public ResponseEntity<ScrapApiResponse> ddd(@CurrentUser LoginUser loginUser) throws Exception {
		ScrapApiResponse scrapApiResponse = scrapService.callScrap(loginUser);
		return ResponseEntity.ok().body(scrapApiResponse);
	}
}
