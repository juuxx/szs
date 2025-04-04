package org.example.szs.domain.taxinfo;

import org.example.szs.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PensionDeductionRepository extends JpaRepository<PensionDeduction, Long> {
	void deleteByTaxInfo(TaxInfo taxInfo);
}
