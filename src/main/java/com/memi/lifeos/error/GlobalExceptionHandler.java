package com.memi.lifeos.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ProblemDetail> onResponseStatus(ResponseStatusException e) {
		ProblemDetail p = ProblemDetail.forStatusAndDetail(
			e.getStatusCode(),
			e.getReason() != null ? e.getReason() : e.getMessage() != null ? e.getMessage() : "Error"
		);
		return ResponseEntity.status(e.getStatusCode()).body(p);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> onValidation(MethodArgumentNotValidException e) {
		String msg = e.getBindingResult().getFieldErrors().stream()
			.map(GlobalExceptionHandler::formatField)
			.collect(Collectors.joining("; "));
		ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
		return ResponseEntity.badRequest().body(p);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ProblemDetail> onNotReadable(HttpMessageNotReadableException e) {
		ProblemDetail p = ProblemDetail.forStatusAndDetail(
			HttpStatus.BAD_REQUEST,
			"Malformed JSON or wrong content type: " + e.getMostSpecificCause().getMessage()
		);
		return ResponseEntity.badRequest().body(p);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> onAny(Exception e) {
		log.error("Unexpected error", e);
		ProblemDetail p = ProblemDetail.forStatusAndDetail(
			HttpStatus.INTERNAL_SERVER_ERROR,
			"An unexpected error occurred"
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(p);
	}

	private static String formatField(FieldError f) {
		return f.getField() + ": " + f.getDefaultMessage();
	}
}
