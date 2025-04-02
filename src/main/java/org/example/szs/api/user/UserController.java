package org.example.szs.api.user;

import java.util.Map;

import org.example.szs.domain.user.UserService;
import org.example.szs.infra.auth.CurrentUser;
import org.example.szs.infra.auth.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	@PostMapping("/szs/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest request) throws Exception {
		userService.signup(request);
		return ResponseEntity.ok().body(Map.of("message", "회원가입 성공"));
	}

	@PostMapping("/szs/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		String token = userService.login(request);
		return ResponseEntity.ok(new LoginResponse(token));
	}

	@GetMapping("/szs/me")
	public ResponseEntity<?> getMe(@CurrentUser LoginUser loginUser) {
		System.out.println(loginUser);
		return ResponseEntity.ok().body(Map.of("login user name", loginUser.name()));
	}

}
