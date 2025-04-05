package org.example.szs.api.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(

	@Schema(description = "사용자 ID", example = "kw68")
	String userId,

	@Schema(description = "비밀번호", example = "123456")
	String password

) {}