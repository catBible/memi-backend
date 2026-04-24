package com.memi.lifeos.supplement.repository;

import com.memi.lifeos.supplement.entity.Supplement;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

public final class SupplementQuerySpecs {

	private SupplementQuerySpecs() {
	}

	/**
	 * @param q optional, matches case-insensitively in name or brand
	 */
	public static Specification<Supplement> byOptionalQuery(String q) {
		return (root, query, cb) -> {
			if (!StringUtils.hasText(q)) {
				return cb.conjunction();
			}
			String p = "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
			Predicate name = cb.like(cb.lower(root.get("name")), p);
			Predicate brand = cb.like(cb.lower(cb.coalesce(root.get("brand"), cb.literal(""))), p);
			return cb.or(name, brand);
		};
	}
}
