package org.example.szs.api.taxinfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.example.szs.infra.feign.dto.ScrapApiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "스크래핑 결과 응답")
public class TaxInfoResponse {

	@Schema(description = "과세 연도", example = "2023")
	private String year;

	@Schema(description = "사용자 이름", example = "동탁")
	@JsonProperty("이름")
	private String name;

	@Schema(description = "종합소득금액", example = "20000000")
	@JsonProperty("종합소득금액")
	private BigDecimal totalIncome;

	@Schema(description = "소득 공제 내역")
	@JsonProperty("소득공제")
	private ScrapDeduction deduction;

	@Getter
	@NoArgsConstructor
	@Schema(description = "소득공제 정보")
	public static class ScrapDeduction {

		@ArraySchema(schema = @Schema(implementation = ScrapPension.class, description = "국민연금 공제 내역"))
		@JsonProperty("국민연금")
		private List<ScrapPension> pension;

		@Schema(description = "신용카드 소득공제 정보")
		@JsonProperty("신용카드소득공제")
		private ScrapCreditCard creditCard;

		@Schema(description = "세액공제", example = "300,000")
		@JsonProperty("세액공제")
		private String taxCredit;
	}

	@Getter
	@NoArgsConstructor
	@Schema(description = "국민연금 공제 데이터")
	public static class ScrapPension {
		@Schema(description = "월", example = "2023-01")
		@JsonProperty("월")
		private String month;

		@Schema(description = "공제액", example = "300,000.25")
		@JsonProperty("공제액")
		private String amount;
	}

	@Getter
	@NoArgsConstructor
	@Schema(description = "신용카드 소득공제 정보")
	public static class ScrapCreditCard {

		@Schema(description = "년도", example = "2023")
		private int year;

		@ArraySchema(schema = @Schema(description = "월별 공제 내역", example = "{\"01\": \"100,000.10\"}"))
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