package org.example.szs.infra.feign.utils;

import java.util.concurrent.Callable;

import org.example.szs.common.error.BusinessException;
import org.example.szs.common.error.ErrorCode;
import org.example.szs.infra.feign.dto.ExternalApiResponse;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScrapClientWrapper {

	private final ObjectMapper objectMapper;

	public <T> T call(Callable<ExternalApiResponse<T>> apiCall, ErrorCode defaultError) {
		try {
			ExternalApiResponse<T> response = apiCall.call();

			if (response == null || response.isFail()) {
				String message = response != null && response.getErrors() != null
					? response.getErrors().getMessage()
					: "알 수 없는 외부 API 실패";

				throw new BusinessException(defaultError, message);
			}

			return response.getData();

		} catch (FeignException.InternalServerError e) {
			log.error("외부 스크래핑 서버 오류 (500): {}", e.getMessage(), e);
			throw new BusinessException(defaultError, "스크래핑 서버가 일시적으로 응답하지 않습니다.");
		} catch (FeignException e) {
			log.error("외부 API 호출 실패: {}", e.getMessage(), e);
			throw new BusinessException(defaultError, "외부 API 응답 실패");
		} catch (Exception e) {
			log.error("예외 발생: {}", e.getMessage(), e);
			throw new BusinessException(defaultError, "외부 API 호출 도중 예외 발생");
		}
	}
}