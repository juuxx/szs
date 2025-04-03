package org.example.szs.domain.user;

import java.util.Map;

import org.example.szs.api.user.LoginRequest;
import org.example.szs.api.user.SignupRequest;
import org.example.szs.common.error.BusinessException;
import org.example.szs.common.error.ErrorCode;
import org.example.szs.infra.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final RegNoEncryptor regNoEncryptor;

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
			throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
		}

		// 2. 중복 체크
		if (userRepository.findByUserId(request.getUserId()).isPresent()) {
			throw new BusinessException(ErrorCode.DUPLICATED_USER_ID);
		}

		// 3. 비밀번호 암호화
		String encryptedPw = passwordEncoder.encode(request.getPassword());

		// 4. 주민등록번호 분리 & 암호화
		EncryptedRegNo encryptedRegNo = regNoEncryptor.encrypt(request.getRegNo());

		// 5. 저장
		User user = User.builder()
			.userId(request.getUserId())
			.password(encryptedPw)
			.name(request.getName())
			.regNoPrefix(encryptedRegNo.prefix())
			.regNoSuffix(encryptedRegNo.suffix())
			.build();

		userRepository.save(user);
	}

	public String login(LoginRequest request) {
		// 1. 사용자 조회
		User user = userRepository.findByUserId(request.userId())
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		// 2. 비밀번호 검증
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
		}

		// 3. JWT 토큰 발급
		return jwtTokenProvider.createToken(user.getUserId());
	}
}
