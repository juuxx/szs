package org.example.szs.domain.user;

import org.example.szs.common.utils.AES256Util;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RegNoEncryptor {

	private final AES256Util aes256Util;

	/**
	 * 암호화된 주민등록번호 데이터 반환
	 */
	public EncryptedRegNo encrypt(String rawRegNo) throws Exception {
		validateFormat(rawRegNo);

		String noDash = rawRegNo.replace("-", "");
		String prefix = noDash.substring(0, 7); // YYMMDDG
		String suffix = noDash.substring(7);    // 뒤 6자리

		String encryptedSuffix = aes256Util.encrypt(suffix);
		return new EncryptedRegNo(prefix, encryptedSuffix);
	}
	/**
	 * 복호화된 주민등록번호 문자열 반환
	 */
	public String decrypt(EncryptedRegNo encrypted) throws Exception {
		String front = encrypted.prefix().substring(0, 6); // 생일
		String gender = encrypted.prefix().substring(6);   // 성별
		String decryptedSuffix = aes256Util.decrypt(encrypted.suffix());
		return front + "-" + gender + decryptedSuffix;
	}

	private void validateFormat(String rawRegNo) {
		if (rawRegNo == null || rawRegNo.length() != 14 || rawRegNo.charAt(6) != '-') {
			throw new IllegalArgumentException("잘못된 주민등록번호 형식입니다.");
		}
	}
}
