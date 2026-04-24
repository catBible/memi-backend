package com.memi.lifeos.supplement.service;

import com.memi.lifeos.supplement.entity.Supplement;
import com.memi.lifeos.supplement.repository.SupplementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class SupplementService {

	private final SupplementRepository repository;

	public SupplementService(SupplementRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<Supplement> list() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public Supplement get(long id) {
		return repository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplement not found: " + id));
	}

	public Supplement create(Supplement body) {
		body.setId(null);
		return repository.save(body);
	}

	public Supplement replace(long id, Supplement body) {
		Supplement existing = get(id);
		existing.setName(body.getName());
		existing.setBrand(body.getBrand());
		existing.setDosage(body.getDosage());
		existing.setForm(body.getForm());
		existing.setNotes(body.getNotes());
		return repository.save(existing);
	}

	public void delete(long id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplement not found: " + id);
		}
		repository.deleteById(id);
	}
}
