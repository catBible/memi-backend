package com.memi.lifeos.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memi.lifeos.LifeOsBackendApplication;
import com.memi.lifeos.task.dto.TaskPatchRequest;
import com.memi.lifeos.task.dto.TaskWriteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LifeOsBackendApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskApiIT {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	private static final String KEY = "X-API-Key";
	private static final String VAL = "memi-dev-api-key-7f2a9c1e";

	@Test
	void listEmptyWrapped() throws Exception {
		mvc.perform(get("/api/tasks"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data").isArray());
	}

	@Test
	void createPatchDeleteTimer() throws Exception {
		var create = new TaskWriteRequest(
			"Buy milk",
			"2%",
			"TODO",
			2,
			List.of("errand"),
			null,
			null,
			Instant.parse("2026-06-01T12:00:00Z"),
			15,
			null,
			null
		);
		String res = mvc.perform(
			post("/api/tasks")
				.header(KEY, VAL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(create))
		)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.id").isNumber())
			.andExpect(jsonPath("$.data.title").value("Buy milk"))
			.andReturn().getResponse().getContentAsString();
		long id = objectMapper.readTree(res).path("data").path("id").asLong();

		mvc.perform(get("/api/tasks/" + id))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value((int) id));

		var patch = new TaskPatchRequest();
		patch.setTitle("Buy oat milk");
		mvc.perform(
			patch("/api/tasks/" + id)
				.header(KEY, VAL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patch))
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.title").value("Buy oat milk"));

		mvc.perform(
			patch("/api/tasks/" + id + "/timer/start").header(KEY, VAL)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.timerStartedAt").isNotEmpty());

		mvc.perform(
			patch("/api/tasks/" + id + "/timer/pause").header(KEY, VAL)
		)
			.andExpect(status().isOk());

		mvc.perform(
			patch("/api/tasks/" + id + "/timer/stop").header(KEY, VAL)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.actualMinutes").exists());

		mvc.perform(
			patch("/api/tasks/" + id + "/schedule")
				.header(KEY, VAL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of(
					"scheduledAt", "2026-05-20T08:00:00Z",
					"dueAt", "2026-05-25T23:59:59Z"
				)))
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.scheduledAt").exists());

		mvc.perform(delete("/api/tasks/" + id).header(KEY, VAL))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true));

		mvc.perform(get("/api/tasks/" + id))
			.andExpect(status().isNotFound());
	}
}
