package org.example.szs.common.config;

import java.util.Optional;

import org.example.szs.infra.auth.LoginUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoginUserAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		Object principal = authentication.getPrincipal();

		if (principal instanceof LoginUser loginUser) {
			return Optional.of(loginUser.userId());
		}

		return Optional.empty();
	}
}
