package org.example.szs.domain.taxinfo;

import org.example.szs.api.taxinfo.TaxInfoResponse;
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
import org.example.szs.infra.feign.utils.ScrapClientWrapper;
import org.springframework.stereotype.Service;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaxScrapProcessor {

	private final ScrapClient scrapClient;
	private final TaxInfoStorage taxInfoStorage;
	private final RegNoEncryptor regNoEncryptor;
	private final UserRepository userRepository;
	private final ScrapClientWrapper scrapClientWrapper;

	@Transactional
	public TaxInfoResponse scrap(LoginUser loginUser) throws Exception {
		User user = userRepository.findById(loginUser.id())
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
		String decryptedRegNo = regNoEncryptor.decrypt(new EncryptedRegNo(user.getRegNoPrefix(), user.getRegNoSuffix()));

		// external api call
		ScrapApiResponse result = scrapClientWrapper.call(
			() -> scrapClient.scrap(new ScrapRequest(user.getName(), decryptedRegNo)),
			ErrorCode.SCRAP_API_FAILED
		);

		// 저장
		taxInfoStorage.save(result, user);

		return TaxInfoResponse.from(result);
	}
}
