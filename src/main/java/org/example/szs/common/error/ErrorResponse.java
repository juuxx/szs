package org.example.szs.common.error;

import org.springframework.http.HttpStatus;

public record ErrorResponse(int status,
							String error,
							String message,
							String path
) {
	public static ErrorResponse of(HttpStatus status, String message, String path) {
		return new ErrorResponse(status.value(), status.name(), message, path);
	}
}
