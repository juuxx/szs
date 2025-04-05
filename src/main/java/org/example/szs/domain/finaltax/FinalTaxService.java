package org.example.szs.domain.finaltax;

import org.example.szs.api.finaltax.FinalTaxResponse;
import org.example.szs.common.error.BusinessException;
import org.example.szs.common.error.ErrorCode;
import org.example.szs.domain.taxinfo.TaxInfo;
import org.example.szs.domain.taxinfo.TaxInfoRepository;
import org.example.szs.domain.user.User;
import org.example.szs.domain.user.UserRepository;
import org.example.szs.infra.auth.LoginUser;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FinalTaxService {

	private final UserRepository userRepository;
	public final TaxInfoRepository taxInfoRepository;

	public FinalTaxResponse getFinalTax(LoginUser loginUser) {
		User user = userRepository.findByUserId(loginUser.userId())
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		TaxInfo taxInfo = taxInfoRepository.findTopByUserOrderByTaxYearDesc(user)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		FinalTaxInput input = new FinalTaxInput(
			taxInfo.getTotalIncomeAmount(),
			taxInfo.getTotalDeductionAmount(),
			taxInfo.getTaxCreditAmount()
		);

		// 계산
		FinalTaxResult output = FinalTaxCalculator.calculate(input);
		return new FinalTaxResponse(output.finalTax());
	}
}
