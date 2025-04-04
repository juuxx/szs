package org.example.szs.domain.taxinfo;

import java.util.List;

import org.example.szs.common.error.BusinessException;
import org.example.szs.common.error.ErrorCode;
import org.example.szs.domain.user.EncryptedRegNo;
import org.example.szs.domain.user.RegNoEncryptor;
import org.example.szs.domain.user.User;
import org.example.szs.domain.user.UserRepository;
import org.example.szs.infra.auth.LoginUser;
import org.example.szs.infra.feign.client.ScrapClient;
import org.example.szs.infra.feign.dto.ScrapApiResponse;
import org.example.szs.infra.feign.dto.ScrapApiStatus;
import org.example.szs.infra.feign.dto.ScrapRequest;
import org.springframework.stereotype.Service;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaxInfoService {

	private final RegNoEncryptor regNoEncryptor;
	private final ScrapClient scrapClient;
	private final UserRepository userRepository;
	private final TaxInfoRepository taxInfoRepository;
	private final PensionDeductionRepository pensionDeductionRepository;
	private final CreditCardDeductionRepository creditCardDeductionRepository;

	@Transactional
	public ScrapApiResponse.ScrapResult callScrap(LoginUser loginUser) throws Exception {
		User user = userRepository.findById(loginUser.id())
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		String decryptedRegNo = regNoEncryptor.decrypt(
			new EncryptedRegNo(user.getRegNoPrefix(), user.getRegNoSuffix())
		);

		ScrapApiResponse response = scrapClient.scrap(
			new ScrapRequest(user.getName(), decryptedRegNo)
		);

		try {
			if (ScrapApiStatus.FAIL.equals(response.getStatus())) {
				handleScrapError(response.getErrors().getMessage(), loginUser.userId());
			}

			if (ScrapApiStatus.SUCCESS.equals(response.getStatus())) {
				// 정상일 경우 TaxInfo, CreditCardDeduction, pensionDeduction 에 해당 값들을 저장 해야됨
				this.saveScrapData(response.getData(), user);
				return response.getData(); // 정상 결과 리턴
			}
		} catch (FeignException fe) {
			log.error("[SCRAP_API_ERROR] status={}, msg={}", fe.status(), fe.getMessage());

			throw new BusinessException(ErrorCode.SCRAP_API_FAILED, "외부 스크래핑 서버에 문제가 발생했습니다.");
		}
		throw new BusinessException(ErrorCode.SCRAP_API_FAILED, "예상치 못한 상태값입니다.");
	}


	private void handleScrapError(String message, String userId) {
		log.warn("[SCRAP_FAIL] userId={}, reason={}", userId, message);

		if (message != null && message.contains("스크랩 가능유저가 아닙니다")) {
			throw new BusinessException(ErrorCode.SCRAP_NOT_ALLOWED);
		}
		throw new BusinessException(ErrorCode.SCRAP_API_FAILED, message);
	}

	@Transactional
	public void saveScrapData(ScrapApiResponse.ScrapResult  result, User user) {
		String year = result.getYear();

		// 1. 기존 데이터 삭제
		taxInfoRepository.findByUserAndTaxYear(user, year)
			.ifPresent(existing -> {
				creditCardDeductionRepository.deleteByTaxInfo(existing);
				pensionDeductionRepository.deleteByTaxInfo(existing);
				taxInfoRepository.delete(existing);
			});

		// 2. 새 TaxInfo 저장
		TaxInfoMapper taxInfoMapper = TaxInfoMapper.of(result, user);
		TaxInfo taxInfo = taxInfoRepository.save(taxInfoMapper.toTaxInfo());

		// 3. 공제 항목들 저장
		List<PensionDeduction> pensions = taxInfoMapper.toPensionDeductions(taxInfo);
		pensionDeductionRepository.saveAll(pensions);

		List<CreditCardDeduction> cards = taxInfoMapper.toCardDeductions(taxInfo);
		creditCardDeductionRepository.saveAll(cards);
	}
}
