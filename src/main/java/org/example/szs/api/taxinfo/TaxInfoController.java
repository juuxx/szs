package org.example.szs.api.taxinfo;

import org.example.szs.domain.taxinfo.TaxScrapProcessor;
import org.example.szs.infra.auth.CurrentUser;
import org.example.szs.infra.auth.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "02-스크래핑", description = "스크래핑 데이터 조회")
@RequiredArgsConstructor
@RestController
public class TaxInfoController {

	private final TaxScrapProcessor taxScrapProcessor;

	@Operation(summary = "스크래핑 데이터 수집", description = "사용자의 스크래핑 데이터를 수집하여 저장합니다.",
		security = @SecurityRequirement(name = "bearer-key"))
	@PostMapping("/szs/scrap")
	public ResponseEntity<TaxInfoResponse> scrapTaxInfo(@Parameter(hidden = true) @CurrentUser LoginUser loginUser) throws Exception {
		TaxInfoResponse response = taxScrapProcessor.scrap(loginUser);
		return ResponseEntity.ok().body(response);
	}
}
