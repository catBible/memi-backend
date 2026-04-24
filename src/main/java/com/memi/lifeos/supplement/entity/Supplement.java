package com.memi.lifeos.supplement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "supplements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;
	private String brand;
	private String dosage;
	private String form;
	@Column(length = 2000)
	private String notes;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;
	private Instant updatedAt;

	@PrePersist
	protected void onCreate() {
		Instant now = Instant.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = Instant.now();
	}
}
