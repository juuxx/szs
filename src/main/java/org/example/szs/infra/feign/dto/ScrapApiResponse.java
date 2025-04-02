package org.example.szs.infra.feign.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrapApiResponse {

	private String status;

	private ScrapResult data;

	private ScrapError errors;

	@Getter
	@NoArgsConstructor
	public static class ScrapResult {
		@JsonProperty("이름")
		private String name;

		@JsonProperty("종합소득금액")
		private int totalIncome;

		@JsonProperty("소득공제")
		private ScrapDeduction deduction;
	}

	@Getter
	@NoArgsConstructor
	public static class ScrapDeduction {
		@JsonProperty("국민연금")
		private List<ScrapPension> pension;

		@JsonProperty("신용카드소득공제")
		private ScrapCreditCard creditCard;

		@JsonProperty("세액공제")
		private String taxCredit;
	}

	@Getter
	@NoArgsConstructor
	public static class ScrapPension {
		@JsonProperty("월")
		private String month;

		@JsonProperty("공제액")
		private String amount;
	}

	@Getter
	@NoArgsConstructor
	public static class ScrapCreditCard {
		private int year;
		private List<Map<String, String>> month;
	}

	@Getter
	@NoArgsConstructor
	public static class ScrapError {
		private String code;
		private String message;
		private Object validations;
	}
}