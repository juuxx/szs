package org.example.szs.common.error;

public enum ErrorCode {
	// Common
	INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
	INTERNAL_SERVER_ERROR(500, "C002", "서버 오류가 발생했습니다."),

	// User
	DUPLICATED_USER_ID(409, "U001", "이미 사용 중인 사용자 ID입니다."),
	UNAUTHORIZED_USER(401, "U002", "인증되지 않은 사용자입니다."),
	FORBIDDEN_USER(403, "U003", "접근 권한이 없습니다."),
	USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
	INVALID_CREDENTIALS(401, "U002", "아이디 또는 비밀번호가 일치하지 않습니다."),
	// Domain-specific
	INVALID_SSN(400, "D001", "유효하지 않은 주민등록번호입니다."),

	SCRAP_NOT_ALLOWED(400, "S001", "스크랩이 불가능한 사용자입니다."),
	SCRAP_API_FAILED(500, "S999", "스크랩 처리 중 문제가 발생했습니다."),
	SCRAP_TIMEOUT(504, "S008", "스크래핑 API 응답 지연"),
	SCRAP_CLIENT_ERROR(400, "S006", "스크래핑 요청이 잘못되었습니다."),
	SCRAP_SERVER_ERROR(502, "S007", "스크래핑 서버 오류 발생"),
	;

	private final int status;
	private final String code;
	private final String message;

	ErrorCode(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public int getStatus() { return status; }
	public String getCode() { return code; }
	public String getMessage() { return message; }
}
