package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository repository;

    @Test
    @DisplayName("WHEN GET all tasks SHOULD return all tasks")
    void httpGetReturnsAllTasks(){
        // given
        int initialSize = repository.findAll().size();
        repository.save(new Task("foo", LocalDateTime.now()));
        repository.save(new Task("bar", LocalDateTime.now()));
        repository.save(new Task("coo", LocalDateTime.now()));

        // when
        Task[] tasks = restTemplate.getForObject("http://localhost:" + port + "/task", Task[].class);

        // then
        assertThat(tasks).hasSize(initialSize + 3);
    }
}