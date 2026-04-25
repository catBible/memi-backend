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
		// Match typical prod shape: several Noon + one Bedtime (UI splits into two stacks)
		repository.save(Supplement.builder()
			.name("Magnesium L-Threonate")
			.brand("Life Extension")
			.dosage("2 Capsules")
			.stockRemaining(60)
			.takenTimeSlot("Bedtime")
			.doseTime("00:30")
			.mealTiming("after")
			.build());
		repository.save(Supplement.builder()
			.name("Vitamin C")
			.brand("Blackmores")
			.dosage("1 Tablet")
			.stockRemaining(30)
			.takenTimeSlot("Noon")
			.doseTime("12:00")
			.mealTiming("after")
			.build());
		repository.save(Supplement.builder()
			.name("Fish Oil")
			.brand("Nordic Naturals")
			.dosage("1 Capsule")
			.stockRemaining(90)
			.takenTimeSlot("Noon")
			.doseTime("12:30")
			.mealTiming("after")
			.build());
		repository.save(Supplement.builder()
			.name("Vitamin B Complex")
			.brand("DHC")
			.dosage("1 Tablet")
			.stockRemaining(60)
			.takenTimeSlot("Noon")
			.doseTime("13:00")
			.mealTiming("before")
			.build());
	}
}
