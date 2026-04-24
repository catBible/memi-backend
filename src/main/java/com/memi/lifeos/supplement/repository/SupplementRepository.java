package com.memi.lifeos.supplement.repository;

import com.memi.lifeos.supplement.entity.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplementRepository extends JpaRepository<Supplement, Long> {
}
