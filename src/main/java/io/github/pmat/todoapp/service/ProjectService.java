package io.github.pmat.todoapp.service;

import io.github.pmat.todoapp.config.TaskConfigurationProperties;
import io.github.pmat.todoapp.model.Project;
import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.model.TaskGroup;
import io.github.pmat.todoapp.model.projection.GroupReadModel;
import io.github.pmat.todoapp.repository.ProjectRepository;
import io.github.pmat.todoapp.repository.TaskGroupRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties configurationProperties;

    public ProjectService(ProjectRepository repository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties configurationProperties) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.configurationProperties = configurationProperties;
    }

    public List<Project> readAll(){
        return repository.findAll();
    }

    public Project save(Project toSave){
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId){
        if(!configurationProperties.isAllowMultipleTasksFromTemplate()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)){
            throw new IllegalStateException("Only one from project is allowed");
        }
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getProjectSteps().stream()
                                .map(step -> new Task(
                                        step.getDescription(),
                                        deadline.plusDays(step.getDaysToDeadline())))
                                .collect(Collectors.toSet())
            );
            return targetGroup;
        }).orElseThrow(IllegalArgumentException::new);
        return new GroupReadModel(result);
    }
}
