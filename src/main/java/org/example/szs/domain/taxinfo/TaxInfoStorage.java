package org.example.szs.domain.taxinfo;

import java.util.List;

import org.example.szs.domain.user.User;
import org.example.szs.infra.feign.dto.ScrapApiResponse;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaxInfoStorage {

	private final TaxInfoRepository taxInfoRepository;
	private final PensionDeductionRepository pensionDeductionRepository;
	private final CreditCardDeductionRepository creditCardDeductionRepository;


	@Transactional
	public void save(ScrapApiResponse result, User user) {
		String year = result.getYear();

		// 1. 기존 데이터 삭제
		taxInfoRepository.findByUserAndTaxYear(user, year)
			.ifPresent(existing -> {
				creditCardDeductionRepository.deleteByTaxInfo(existing);
				pensionDeductionRepository.deleteByTaxInfo(existing);
				taxInfoRepository.delete(existing);
			});

		// 2. 새 TaxInfo 저장
		TaxInfoMapper taxInfoMapper = TaxInfoMapper.of(result, user);
		TaxInfo taxInfo = taxInfoRepository.save(taxInfoMapper.toTaxInfo());

		// 3. 공제 항목들 저장
		List<PensionDeduction> pensions = taxInfoMapper.toPensionDeductions(taxInfo);
		pensionDeductionRepository.saveAll(pensions);

		List<CreditCardDeduction> cards = taxInfoMapper.toCardDeductions(taxInfo);
		creditCardDeductionRepository.saveAll(cards);
	}
}
