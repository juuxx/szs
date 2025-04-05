package org.example.szs.domain.taxinfo;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.example.szs.common.utils.AmountUtil;
import org.example.szs.common.utils.DateUtil;
import org.example.szs.domain.user.User;
import org.example.szs.infra.feign.dto.ScrapApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaxInfoMapper {

	private final ScrapApiResponse result;
	private final User user;

	public static TaxInfoMapper of(ScrapApiResponse result, User user) {
		return new TaxInfoMapper(result, user);
	}

	public TaxInfo toTaxInfo() {
		return TaxInfo.builder()
			.user(user)
			.taxYear(result.getYear())
			.totalIncomeAmount(new BigDecimal(String.valueOf(result.getTotalIncome())))
			.taxCreditAmount(AmountUtil.parse(result.getDeduction().getTaxCredit()))
			.build();
	}

	public List<PensionDeduction> toPensionDeductions(TaxInfo taxInfo) {
		return result.getDeduction().getPension().stream()
			.map(d -> {
				YearMonth ym = DateUtil.parseYearMonth(d.getMonth());
				return PensionDeduction.builder()
					.user(user)
					.taxInfo(taxInfo)
					.year(String.valueOf(ym.getYear()))
					.month(DateUtil.formatMonth(ym.getMonthValue()))
					.amount(AmountUtil.parse(d.getAmount()))
					.build();
				}
			).toList();
	}

	public List<CreditCardDeduction> toCardDeductions(TaxInfo taxInfo) {
		return result.getDeduction().getCreditCard().getMonth().stream()
			.map(entry -> {
				Map.Entry<String, String> e = entry.entrySet().iterator().next();
				return CreditCardDeduction.builder()
					.user(user)
					.taxInfo(taxInfo)
					.year(String.valueOf(result.getDeduction().getCreditCard().getYear()))
					.month(e.getKey())
					.amount(AmountUtil.parse(e.getValue()))
					.build();
			}).toList();
	}

}
