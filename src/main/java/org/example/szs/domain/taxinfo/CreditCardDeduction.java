package org.example.szs.domain.taxinfo;

import java.math.BigDecimal;

import org.example.szs.domain.common.AbstractAuditingEntity;
import org.example.szs.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_credit_card_deduction", uniqueConstraints = {
	@UniqueConstraint(name = "uk_credit_card_deduction_id",  columnNames = {"user_id", "tax_year", "tax_month"})
})
@Getter
@NoArgsConstructor
public class CreditCardDeduction extends AbstractAuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "tax_year", nullable = false, length = 4)
	private String year;

	@Column(name = "tax_month", nullable = false, length = 2)
	private String month;

	@Column(name = "amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal amount;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_info_id")
	private TaxInfo taxInfo;

	@Builder
	public CreditCardDeduction(Long id, User user, String year, String month, BigDecimal amount, TaxInfo taxInfo) {
		this.id = id;
		this.user = user;
		this.year = year;
		this.month = month;
		this.amount = amount;
		this.taxInfo = taxInfo;
	}
}
