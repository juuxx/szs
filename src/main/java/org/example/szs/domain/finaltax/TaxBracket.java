package org.example.szs.domain.finaltax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaxBracket {

	UP_TO_14M(
		new IncomeRange(new BigDecimal("0"), new BigDecimal("14000000")),
		new BigDecimal("0.06"),
		BigDecimal.ZERO
	),
	BETWEEN_14M_AND_50M(
		new IncomeRange(new BigDecimal("14000000"), new BigDecimal("50000000")),
		new BigDecimal("0.15"),
		new BigDecimal("840000")
	),
	BETWEEN_50M_AND_88M(
		new IncomeRange(new BigDecimal("50000000"), new BigDecimal("88000000")),
		new BigDecimal("0.24"),
		new BigDecimal("6240000")
	),
	BETWEEN_88M_AND_150M(
		new IncomeRange(new BigDecimal("88000000"), new BigDecimal("150000000")),
		new BigDecimal("0.35"),
		new BigDecimal("15360000")
	),
	BETWEEN_150M_AND_300M(
		new IncomeRange(new BigDecimal("150000000"), new BigDecimal("300000000")),
		new BigDecimal("0.38"),
		new BigDecimal("37060000")
	),
	BETWEEN_300M_AND_500M(
		new IncomeRange(new BigDecimal("300000000"), new BigDecimal("500000000")),
		new BigDecimal("0.40"),
		new BigDecimal("94060000")
	),
	BETWEEN_500M_AND_1B(
		new IncomeRange(new BigDecimal("500000001"), new BigDecimal("1000000000")),
		new BigDecimal("0.42"),
		new BigDecimal("174060000")
	),
	OVER_1B(
		new IncomeRange(new BigDecimal("1000000001"), null),
		new BigDecimal("0.45"),
		new BigDecimal("384060000")
	);

	private final IncomeRange range;
	private final BigDecimal rate;
	private final BigDecimal baseTax;

	public static TaxBracket find(BigDecimal taxableIncome) {
		return Arrays.stream(values())
			.filter(bracket -> bracket.range.contains(taxableIncome))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("해당하는 과세 구간이 없습니다."));
	}

	public BigDecimal calculate(BigDecimal taxableIncome) {
		BigDecimal overAmount = taxableIncome.subtract(range.getMin()).max(BigDecimal.ZERO);
		BigDecimal stepTax = overAmount.multiply(rate).setScale(0, RoundingMode.HALF_UP);
		return baseTax.add(stepTax);
	}
}