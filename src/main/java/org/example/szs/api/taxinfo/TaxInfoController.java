package org.example.szs.api.taxinfo;

import org.example.szs.domain.taxinfo.TaxInfoService;
import org.example.szs.domain.taxinfo.TaxScrapProcessor;
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

	private final TaxScrapProcessor taxScrapProcessor;

	@PostMapping("/szs/scrap")
	public ResponseEntity<TaxInfoResponse> scrapTaxInfo(@CurrentUser LoginUser loginUser) throws Exception {
		TaxInfoResponse response = taxScrapProcessor.scrap(loginUser);
		return ResponseEntity.ok().body(response);
	}
}
