package org.example.szs.api.taxinfo;

import org.example.szs.domain.taxinfo.TaxInfoService;
import org.example.szs.infra.auth.CurrentUser;
import org.example.szs.infra.auth.LoginUser;
import org.example.szs.infra.feign.dto.ScrapApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TaxInfoController {

	private final TaxInfoService taxInfoService;

	@PostMapping("/szs/scrap")
	public ResponseEntity<ScrapApiResponse.ScrapResult> ddd(@CurrentUser LoginUser loginUser) throws Exception {
		ScrapApiResponse.ScrapResult scrapApiResponse = taxInfoService.callScrap(loginUser);
		return ResponseEntity.ok().body(scrapApiResponse);
	}
}
