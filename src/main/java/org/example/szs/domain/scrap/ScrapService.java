package org.example.szs.domain.scrap;

import org.example.szs.common.error.BusinessException;
import org.example.szs.common.error.ErrorCode;
import org.example.szs.common.utils.AES256Util;
import org.example.szs.domain.user.User;
import org.example.szs.domain.user.UserRepository;
import org.example.szs.infra.auth.LoginUser;
import org.example.szs.infra.feign.client.ScrapClient;
import org.example.szs.infra.feign.config.ScrapApiProperties;
import org.example.szs.infra.feign.dto.ScrapApiResponse;
import org.example.szs.infra.feign.dto.ScrapRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapService {

	private final ScrapClient scrapClient;
	private final AES256Util aes256Util;
	private final UserRepository userRepository;
	private final ScrapApiProperties scrapApiProperties;

	public ScrapApiResponse callScrap(LoginUser loginUser) throws Exception {
		User user = userRepository.findById(loginUser.id()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		String regNoPrefix = user.getRegNoPrefix(); // 7자리: YYMMDDG
		String decryptedSuffix = aes256Util.decrypt(user.getRegNoSuffix()); // 6자리

		String front = regNoPrefix.substring(0, 6); // YYMMDD
		String gender = regNoPrefix.substring(6);   // G

		String regNo = front + "-" + gender + decryptedSuffix;

		// 요청 생성
		ScrapRequest req = new ScrapRequest(user.getName(), regNo);
		log.info("스크랩 요청: name={}, regNo={}", req.name(), req.regNo());
		return scrapClient.scrap(req);
	}
}
