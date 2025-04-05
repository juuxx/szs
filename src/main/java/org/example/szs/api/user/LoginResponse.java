package org.example.szs.api.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(

	@Schema(description = "JWT Access Token", example = "Bearer token...")
	String accessToken

) {}
