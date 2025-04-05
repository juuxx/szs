package org.example.szs.api.user;

import java.util.Map;

import org.example.szs.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "01-인증", description = "로그인 / 회원가입 API")
@RequestMapping
@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	@Operation(summary = "회원가입", description = "이름, 주민등록번호, 사용자 ID, 비밀번호를 입력하여 회원가입합니다.")
	@PostMapping("/szs/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest request) throws Exception {
		userService.signup(request);
		return ResponseEntity.ok().body(Map.of("message", "회원가입 성공"));
	}

	@Operation(summary = "로그인", description = "사용자 ID, 비밀번호를 입력하여 로그인합니다.")
	@PostMapping("/szs/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		String token = userService.login(request);
		return ResponseEntity.ok(new LoginResponse(token));
	}
}
