package io.github.pmat.todoapp.config;

import io.github.pmat.todoapp.repository.ProjectRepository;
import io.github.pmat.todoapp.repository.TaskGroupRepository;
import io.github.pmat.todoapp.repository.TaskRepository;
import io.github.pmat.todoapp.service.ProjectService;
import io.github.pmat.todoapp.service.TaskGroupService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Alternative for using @Service annotation in services.
@Configuration
public class LogicConfiguration {

    @Bean
    ProjectService projectService(ProjectRepository repository,
                                  TaskGroupRepository taskGroupRepository,
                                  TaskConfigurationProperties config,
                                  TaskGroupService taskGroupService) {
        return new ProjectService(repository, taskGroupRepository, config, taskGroupService);
    }

    @Bean
    TaskGroupService taskGroupService(TaskGroupRepository taskGroupRepository,
                                      TaskRepository taskRepository) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
