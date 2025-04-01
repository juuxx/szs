package org.example.szs.common.security;

import static org.springframework.security.config.Customizer.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf
				.ignoringRequestMatchers("/h2-console/**", "/szs/signup") // CSRF 무시
			)
			.headers(headers -> headers
				.frameOptions(frameOptions -> frameOptions.sameOrigin()) // H2 iframe 허용
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/h2-console/**", "/szs/signup").permitAll() // 누구나 접근 가능
				.anyRequest().authenticated() // 나머지는 인증 필요
			)
			.formLogin(withDefaults()); // 로그인 폼 (JWT 붙이기 전까지는 개발용)

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
