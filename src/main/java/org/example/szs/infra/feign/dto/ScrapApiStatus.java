package org.example.szs.infra.feign.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ScrapApiStatus {
	SUCCESS, FAIL;

	@JsonCreator
	public static ScrapApiStatus from(String value) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown status: " + value));
	}
}
