package com.memi.lifeos.supplement.api;

import com.memi.lifeos.supplement.dto.SupplementResponse;
import com.memi.lifeos.supplement.dto.SupplementWriteRequest;
import com.memi.lifeos.supplement.service.SupplementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/supplements")
public class SupplementController {

	private final SupplementService supplementService;

	public SupplementController(SupplementService supplementService) {
		this.supplementService = supplementService;
	}

	@GetMapping
	@Operation(summary = "List supplements (paged, optional name/brand search)")
	public Page<SupplementResponse> list(
		@PageableDefault(size = 20) Pageable page,
		@Parameter(description = "search in name and brand, case-insensitive")
		@RequestParam(required = false) String q) {
		return supplementService.list(page, q);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get one supplement by id")
	public SupplementResponse get(@PathVariable long id) {
		return supplementService.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create supplement", security = @SecurityRequirement(name = "X-API-Key"))
	public SupplementResponse create(@Valid @RequestBody SupplementWriteRequest body) {
		return supplementService.create(body);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Full replace (PUT) supplement", security = @SecurityRequirement(name = "X-API-Key"))
	public SupplementResponse replace(@PathVariable long id, @Valid @RequestBody SupplementWriteRequest body) {
		return supplementService.replace(id, body);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete supplement", security = @SecurityRequirement(name = "X-API-Key"))
	public void delete(@PathVariable long id) {
		supplementService.delete(id);
	}
}
