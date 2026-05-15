package com.memi.lifeos.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Standard envelope for task API (and future resources). {@code data} may be null for deletes.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(boolean success, T data, String message) {

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(true, data, null);
	}

	public static ApiResponse<Void> okMessage(String message) {
		return new ApiResponse<>(true, null, message);
	}
}
