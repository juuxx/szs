package org.example.szs.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
		ErrorCode fallbackCode = ErrorCode.INTERNAL_SERVER_ERROR;

		return ResponseEntity.status(fallbackCode.getStatus())
			.body(new ErrorResponse(
				fallbackCode.getStatus(),
				fallbackCode.getCode(),
				ex.getMessage(),
				request.getRequestURI()
			));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
		ErrorCode code = ex.getErrorCode();
		return ResponseEntity.status(code.getStatus())
			.body(new ErrorResponse(code.getStatus(), code.getCode(), code.getMessage(), request.getRequestURI()));
	}
}
