package com.memi.lifeos.supplement.dto;

import com.memi.lifeos.supplement.entity.Supplement;
import org.springframework.data.domain.Page;

import java.util.Objects;

public final class SupplementMapper {

	private SupplementMapper() {
	}

	public static Supplement toNewEntity(SupplementWriteRequest w) {
		Objects.requireNonNull(w, "w");
		return Supplement.builder()
			.name(w.name().trim())
			.brand(trimOrNull(w.brand()))
			.dosage(trimOrNull(w.dosage()))
			.stockRemaining(w.stockRemaining())
			.takenTimeSlot(trimOrNull(w.takenTimeSlot()))
			.doseTime(trimOrNull(w.doseTime()))
			.mealTiming(normalizeMealTiming(w.mealTiming()))
			.build();
	}

	public static void copy(SupplementWriteRequest w, Supplement target) {
		Objects.requireNonNull(w, "w");
		Objects.requireNonNull(target, "target");
		target.setName(w.name().trim());
		target.setBrand(trimOrNull(w.brand()));
		target.setDosage(trimOrNull(w.dosage()));
		target.setStockRemaining(w.stockRemaining());
		target.setTakenTimeSlot(trimOrNull(w.takenTimeSlot()));
		target.setDoseTime(trimOrNull(w.doseTime()));
		target.setMealTiming(normalizeMealTiming(w.mealTiming()));
	}

	public static SupplementResponse toResponse(Supplement s) {
		return new SupplementResponse(
			s.getId(),
			s.getName(),
			s.getBrand(),
			s.getDosage(),
			s.getStockRemaining(),
			s.getTakenTimeSlot(),
			s.getDoseTime(),
			s.getMealTiming(),
			s.getLastTakenAt()
		);
	}

	public static Page<SupplementResponse> toResponsePage(Page<Supplement> page) {
		return page.map(SupplementMapper::toResponse);
	}

	private static String trimOrNull(String v) {
		if (v == null) {
			return null;
		}
		String t = v.trim();
		return t.isEmpty() ? null : t;
	}

	/** Store lowercase for stable API. */
	private static String normalizeMealTiming(String v) {
		if (v == null) {
			return null;
		}
		String t = v.trim();
		if (t.isEmpty()) {
			return null;
		}
		String l = t.toLowerCase();
		if ("before".equals(l) || "after".equals(l)) {
			return l;
		}
		return t;
	}
}
