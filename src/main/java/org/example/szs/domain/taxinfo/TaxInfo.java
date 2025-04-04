package org.example.szs.domain.taxinfo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.example.szs.domain.common.AbstractAuditingEntity;
import org.example.szs.domain.user.User;
import org.example.szs.infra.feign.dto.ScrapApiResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_tax_info",
	uniqueConstraints = @UniqueConstraint(name = "uk_tax_info_user_year", columnNames = {"user_id", "tax_year"}))
@Getter
@NoArgsConstructor
public class TaxInfo extends AbstractAuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// 과세연도 (예: 2023)
	@Column(name = "tax_year", nullable = false)
	private String taxYear;

	// 종합소득금액
	@Column(name = "total_income_amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal totalIncomeAmount;

	// 세액공제
	@Column(name = "tax_credit_amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal taxCreditAmount;

	// 국민연금 공제 내역
	@OneToMany(mappedBy = "taxInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PensionDeduction> pensionDeductions = new ArrayList<>();

	// 신용카드 공제 내역
	@OneToMany(mappedBy = "taxInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CreditCardDeduction> creditCardDeductions = new ArrayList<>();

	public static TaxInfo of(ScrapApiResponse.ScrapResult result, User user) {
		return null;
	}

	public void addPensionDeduction(PensionDeduction deduction) {
		pensionDeductions.add(deduction);
		deduction.setTaxInfo(this);
	}

	public void addCreditCardDeduction(CreditCardDeduction deduction) {
		creditCardDeductions.add(deduction);
		deduction.setTaxInfo(this);
	}

	@Builder
	public TaxInfo(User user, String taxYear, BigDecimal totalIncomeAmount, BigDecimal taxCreditAmount) {
		this.user = user;
		this.taxYear = taxYear;
		this.totalIncomeAmount = totalIncomeAmount;
		this.taxCreditAmount = taxCreditAmount;
	}

	public BigDecimal getTotalDeductionAmount() {
		BigDecimal pensionSum = pensionDeductions.stream()
			.map(PensionDeduction::getAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal cardSum = creditCardDeductions.stream()
			.map(CreditCardDeduction::getAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		return pensionSum.add(cardSum).add(taxCreditAmount);
	}
}
