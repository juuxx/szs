package org.example.szs.domain.user;

import java.util.Map;

import org.example.szs.api.user.SignupRequest;
import org.example.szs.common.utils.AES256Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AES256Util aes256Util;

	private static final Map<String, String> WHITELIST = Map.of(
		"동탁", "921108-1582816",
		"관우", "681108-1582816",
		"손권", "890601-2455116",
		"유비", "790411-1656116",
		"조조", "810326-2715702"
	);

	public void signup(SignupRequest request) throws Exception {
		// 1. 화이트리스트 체크
		if (!WHITELIST.containsKey(request.getName()) ||
			!WHITELIST.get(request.getName()).equals(request.getRegNo())) {
			throw new IllegalArgumentException("가입 불가능한 사용자입니다.");
		}

		// 2. 중복 체크
		if (userRepository.findByUserId(request.getUserId()).isPresent()) {
			throw new IllegalStateException("이미 존재하는 사용자 ID입니다.");
		}

		// 3. 비밀번호 암호화
		String encryptedPw = passwordEncoder.encode(request.getPassword());

		// 4. 주민등록번호 분리 & 암호화
		String regNo = request.getRegNo().replace("-", ""); // ex: "921108-1582816"
		String regNoPrefix = regNo.substring(0, 7); // "9211081"
		String regNoSuffix = regNo.substring(8);    // "582816"
		String encryptedSuffix = aes256Util.encrypt(regNoSuffix);

		// 5. 저장
		User user = User.builder()
			.userId(request.getUserId())
			.password(encryptedPw)
			.name(request.getName())
			.regNoPrefix(regNoPrefix)
			.regNoSuffix(encryptedSuffix)
			.build();

		userRepository.save(user);
	}
}
