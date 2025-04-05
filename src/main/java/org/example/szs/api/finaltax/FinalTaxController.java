package org.example.szs.api.finaltax;

import org.example.szs.domain.finaltax.FinalTaxService;
import org.example.szs.infra.auth.CurrentUser;
import org.example.szs.infra.auth.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "03-산출세액", description = "산출세액 계산")
@RequiredArgsConstructor
@RestController
public class FinalTaxController {

	private final FinalTaxService finalTaxService;

	@Operation(summary = "결정세액 계산",
		description = """
        - 과세표준 = 종합소득금액 - 소득공제
        - 산출세액 = 과세표준 * 기본세율
        - 결정세액 = 산출세액 - 세액공제
        """,
		security = @SecurityRequirement(name = "bearer-key"))
	@GetMapping("/szs/refund")
	public ResponseEntity<FinalTaxResponse> calculateFinalTax(@Parameter(hidden = true) @CurrentUser LoginUser loginUser){
		FinalTaxResponse response = finalTaxService.getFinalTax(loginUser);
		return ResponseEntity.ok(response);
	}

}
