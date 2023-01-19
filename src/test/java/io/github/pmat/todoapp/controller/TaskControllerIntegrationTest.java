package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repository;

    @Test
    @DisplayName("GIVEN test task saved WHEN GET added task THEN should return result code 200")
    void httpGetReturnsGivenTasks() throws Exception {
        // given
        int id = repository.save(new Task("foo", LocalDateTime.now())).getId();

        // when, then
        mockMvc.perform(get("/task/" + id))
                .andExpect(status().is(200));
    }
}

