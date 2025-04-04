package org.example.szs.domain.taxinfo;

import java.util.Optional;

import org.example.szs.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxInfoRepository extends JpaRepository<TaxInfo, Long> {
	Optional<TaxInfo> findByUserAndTaxYear(User user, String year);
}
