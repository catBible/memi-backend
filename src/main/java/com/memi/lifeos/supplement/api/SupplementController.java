package com.memi.lifeos.supplement.api;

import com.memi.lifeos.supplement.entity.Supplement;
import com.memi.lifeos.supplement.service.SupplementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/supplements")
public class SupplementController {

	private final SupplementService supplementService;

	public SupplementController(SupplementService supplementService) {
		this.supplementService = supplementService;
	}

	@GetMapping
	public List<Supplement> list() {
		return supplementService.list();
	}

	@GetMapping("/{id}")
	public Supplement get(@PathVariable long id) {
		return supplementService.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Supplement create(@Valid @RequestBody Supplement body) {
		return supplementService.create(body);
	}

	@PutMapping("/{id}")
	public Supplement replace(@PathVariable long id, @Valid @RequestBody Supplement body) {
		return supplementService.replace(id, body);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		supplementService.delete(id);
	}
}
