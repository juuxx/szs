package org.example.szs.infra.jwt;

import java.io.IOException;
import java.util.List;

import org.example.szs.domain.user.User;
import org.example.szs.domain.user.UserRepository;
import org.example.szs.infra.auth.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		System.out.println("Auth Header = " + header);
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			if (jwtTokenProvider.validateToken(token)) {
				String userId = jwtTokenProvider.extractUserId(token);
				System.out.println("userId from token = " + userId);
				User user = userRepository.findByUserId(userId)
					.orElseThrow(() -> new UsernameNotFoundException("User not found"));

				LoginUser loginUser = new LoginUser(user.getId(), user.getUserId(), user.getName());

				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(loginUser, null, List.of());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}else {
				System.out.println("❌ Invalid Token");
			}
		}

		filterChain.doFilter(request, response);
	}
}
