package com.memi.lifeos.supplement;

import com.memi.lifeos.supplement.entity.Supplement;
import com.memi.lifeos.supplement.repository.SupplementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fills the in-memory H2 DB for local dev when {@code spring.profiles.active} includes {@code h2}.
 * Run the app with: {@code mvnw spring-boot:run -Dspring-boot.run.profiles=h2} (or {@code set SPRING_PROFILES_ACTIVE=h2}).
 */
@Component
@Profile("h2")
@Order(0)
class H2SupplementDevSeed implements CommandLineRunner {

	private final SupplementRepository repository;

	H2SupplementDevSeed(SupplementRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (repository.count() > 0) {
			return;
		}
		repository.save(Supplement.builder()
			.name("Vitamin D3")
			.brand("Nordic Naturals")
			.dosage("2000 IU")
			.form("softgel")
			.notes("Take with breakfast")
			.build());
		repository.save(Supplement.builder()
			.name("Omega-3 (EPA/DHA)")
			.brand("Carlson")
			.dosage("1000 mg")
			.form("softgel")
			.notes(null)
			.build());
	}
}
