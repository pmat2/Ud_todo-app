package io.github.pmat.todoapp.service;

import io.github.pmat.todoapp.config.TaskConfigurationProperties;
import io.github.pmat.todoapp.model.Project;
import io.github.pmat.todoapp.model.projection.GroupReadModel;
import io.github.pmat.todoapp.model.projection.GroupWriteModel;
import io.github.pmat.todoapp.model.projection.ProjectWriteModel;
import io.github.pmat.todoapp.model.projection.TaskWriteModel;
import io.github.pmat.todoapp.repository.ProjectRepository;
import io.github.pmat.todoapp.repository.TaskGroupRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service - used LogicConfiguration.class instead
public class ProjectService {

    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties configurationProperties;

    private TaskGroupService taskGroupService;

    public ProjectService(ProjectRepository repository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties configurationProperties, TaskGroupService taskGroupService) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.configurationProperties = configurationProperties;
        this.taskGroupService = taskGroupService;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(ProjectWriteModel toSave) {
        return repository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!configurationProperties.isAllowMultipleTasksFromTemplate()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one from project is allowed");
        }
        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getProjectSteps().stream()
                                    .map(step -> {
                                        var task = new TaskWriteModel();
                                        task.setDescription(step.getDescription());
                                        task.setDeadline(deadline.plusDays(step.getDaysToDeadline()));
                                        return task;
                                    })
                                    .collect(Collectors.toSet())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
