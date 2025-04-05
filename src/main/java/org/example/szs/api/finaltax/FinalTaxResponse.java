package org.example.szs.api.finaltax;

import java.math.BigDecimal;

import org.example.szs.common.utils.CommaNumberSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "결정세액 계산 결과 응답")
@AllArgsConstructor
@Getter
public class FinalTaxResponse {

	@Schema(description = "결정세액", example = "150,000")
	@JsonSerialize(using = CommaNumberSerializer.class)
	@JsonProperty("결정세액")
	private BigDecimal finalTaxAmount;
}
