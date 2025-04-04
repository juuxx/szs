package org.example.szs.common.utils;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

public class DateUtil {
	private DateUtil() {}

	public static YearMonth parseYearMonth(String raw) {
		try {
			return YearMonth.parse(raw); // ISO 포맷 "2023-10"
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("잘못된 연월 형식입니다: " + raw);
		}
	}

	public static String formatMonth(int month) {
		return String.format("%02d", month); // 1 → "01"
	}
}
