package org.example.szs.api.taxinfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.example.szs.infra.feign.dto.ScrapApiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaxInfoResponse {

	private String year;

	@JsonProperty("이름")
	private String name;

	@JsonProperty("종합소득금액")
	private BigDecimal totalIncome;

	@JsonProperty("소득공제")
	private ScrapDeduction deduction;

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

	public static TaxInfoResponse from(ScrapApiResponse result) {
		TaxInfoResponse response = new TaxInfoResponse();
		response.year = result.getYear();
		response.name = result.getName();
		response.totalIncome = result.getTotalIncome();

		ScrapApiResponse.ScrapDeduction source = result.getDeduction();
		ScrapDeduction target = new ScrapDeduction();

		// 국민연금 변환
		List<ScrapPension> pensions = source.getPension().stream()
			.map(p -> {
				ScrapPension pension = new ScrapPension();
				pension.month = p.getMonth();
				pension.amount = p.getAmount();
				return pension;
			}).toList();

		// 신용카드 변환
		ScrapCreditCard card = new ScrapCreditCard();
		card.year = source.getCreditCard().getYear();
		card.month = source.getCreditCard().getMonth();

		// 조립
		target.pension = pensions;
		target.creditCard = card;
		target.taxCredit = source.getTaxCredit();

		response.deduction = target;
		return response;
	}
}
