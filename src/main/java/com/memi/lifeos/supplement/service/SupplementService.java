package com.memi.lifeos.supplement.service;

import com.memi.lifeos.supplement.dto.SupplementMapper;
import com.memi.lifeos.supplement.dto.SupplementResponse;
import com.memi.lifeos.supplement.dto.SupplementWriteRequest;
import com.memi.lifeos.supplement.entity.Supplement;
import com.memi.lifeos.supplement.repository.SupplementQuerySpecs;
import com.memi.lifeos.supplement.repository.SupplementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
	public Page<SupplementResponse> list(Pageable pageable, String q) {
		Specification<Supplement> spec = SupplementQuerySpecs.byOptionalQuery(q);
		Page<Supplement> all = repository.findAll(spec, pageable);
		return SupplementMapper.toResponsePage(all);
	}

	@Transactional(readOnly = true)
	public List<SupplementResponse> listAll(String q) {
		Specification<Supplement> spec = SupplementQuerySpecs.byOptionalQuery(q);
		return repository.findAll(spec).stream()
			.map(SupplementMapper::toResponse)
			.toList();
	}

	@Transactional(readOnly = true)
	public SupplementResponse get(long id) {
		return SupplementMapper.toResponse(
			repository.findById((int) id)
				.orElseThrow(() -> notFound(id))
		);
	}

	public SupplementResponse create(SupplementWriteRequest body) {
		Supplement s = repository.save(SupplementMapper.toNewEntity(body));
		return SupplementMapper.toResponse(s);
	}

	public SupplementResponse replace(long id, SupplementWriteRequest body) {
		int sid = (int) id;
		Supplement existing = repository.findById(sid).orElseThrow(() -> notFound(id));
		SupplementMapper.copy(body, existing);
		return SupplementMapper.toResponse(repository.save(existing));
	}

	public void delete(long id) {
		int sid = (int) id;
		if (!repository.existsById(sid)) {
			throw notFound(id);
		}
		repository.deleteById(sid);
	}

	private static ResponseStatusException notFound(long id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplement not found: " + id);
	}
}
