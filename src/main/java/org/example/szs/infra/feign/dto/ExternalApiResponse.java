package org.example.szs.infra.feign.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공통된 형태로 response 받는다는 가정하에 처리
 * @param <T>
 */
@Getter
@NoArgsConstructor
public class ExternalApiResponse<T> {
	private String status;
	private T data;
	private ErrorBody errors;

	@Getter
	@NoArgsConstructor
	public static class ErrorBody {
		private String code;
		private String message;
	}

	public boolean isSuccess() {
		return "success".equalsIgnoreCase(status);
	}

	public boolean isFail() {
		return "fail".equalsIgnoreCase(status);
	}
}
