package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.repository.TaskRepository;
import io.github.pmat.todoapp.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@ActiveProfiles("integration")
public class TaskControllerLightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository repository;

    @MockBean
    private TaskService service;

    @Test
    @DisplayName("GIVEN test task saved WHEN GET added task THEN should return result code 2xx AND contains test task description")
    void httpGetReturnsGivenTasks() throws Exception {
        //given
        String desc = "foo";
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(new Task(desc, LocalDateTime.now())));

        // when, then
        mockMvc.perform(get("/task/123"))
                .andDo(print())
                .andExpect(content().string(containsString(desc)))
                .andExpect(status().is2xxSuccessful());
    }
}
