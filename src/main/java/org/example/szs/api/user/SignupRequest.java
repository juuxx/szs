package org.example.szs.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SignupRequest {

	@Schema(description = "사용자 ID", example = "kw68", requiredMode = Schema.RequiredMode.REQUIRED)
	private String userId;

	@Schema(description = "비밀번호", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
	private String password;

	@Schema(description = "이름", example = "관우", requiredMode = Schema.RequiredMode.REQUIRED)
	private String name;

	@Schema(
		description = "주민등록번호 (앞 6자리 + '-' + 뒤 7자리)",
		example = "681108-1582816",
		requiredMode = Schema.RequiredMode.REQUIRED,
		pattern = "\\d{6}-[1-4]\\d{6}"
	)
	private String regNo;
}