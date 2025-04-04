package org.example.szs.common.utils;

import java.math.BigDecimal;

public class AmountUtil {
	private AmountUtil() {}

	public static BigDecimal parse(String amountStr) {
		return new BigDecimal(amountStr.replace(",", ""));
	}
}
