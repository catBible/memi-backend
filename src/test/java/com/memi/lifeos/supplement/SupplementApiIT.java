package com.memi.lifeos.supplement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memi.lifeos.LifeOsBackendApplication;
import com.memi.lifeos.supplement.dto.SupplementWriteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LifeOsBackendApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SupplementApiIT {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	private static final String KEY = "X-API-Key";
	private static final String VAL = "test-api-key";

	@Test
	void listEmpty() throws Exception {
		mvc.perform(get("/api/supplements"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.empty").value(true));
	}

	@Test
	void postWithoutKeyIsUnauthorized() throws Exception {
		var body = new SupplementWriteRequest("Test", "b", "1", "tab", "n");
		mvc.perform(
			post("/api/supplements")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(body))
		)
			.andExpect(status().isUnauthorized());
	}

	@Test
	void createGetDelete() throws Exception {
		var create = new SupplementWriteRequest("Omega-3", "Nordic", "1000mg", "softgel", null);
		String res = mvc.perform(
			post("/api/supplements")
				.header(KEY, VAL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(create))
		)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNumber())
			.andExpect(jsonPath("$.name").value("Omega-3"))
			.andReturn()
			.getResponse()
			.getContentAsString();
		long id = objectMapper.readTree(res).get("id").asLong();

		mvc.perform(get("/api/supplements/" + id))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id))
			.andExpect(jsonPath("$.dosage").value("1000mg"));

		mvc.perform(delete("/api/supplements/" + id).header(KEY, VAL))
			.andExpect(status().isNoContent());
	}
}
