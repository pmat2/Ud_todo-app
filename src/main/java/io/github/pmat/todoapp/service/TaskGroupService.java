package io.github.pmat.todoapp.service;

import io.github.pmat.todoapp.model.Project;
import io.github.pmat.todoapp.model.TaskGroup;
import io.github.pmat.todoapp.model.projection.GroupReadModel;
import io.github.pmat.todoapp.model.projection.GroupWriteModel;
import io.github.pmat.todoapp.repository.TaskGroupRepository;
import io.github.pmat.todoapp.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

//@Service - used LogicConfiguration.class instead
public class TaskGroupService {
    private TaskGroupRepository taskGroupRepository;
    private TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.taskGroupRepository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    public GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = taskGroupRepository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return taskGroupRepository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        TaskGroup result = taskGroupRepository.findById(groupId)
                .orElseThrow(() ->new IllegalArgumentException("Task group with given ID not found"));

        if (taskRepository.existsByDoneIsFalseAndGroupId(groupId)
                && !result.isDone()) {
            throw new IllegalStateException("Group has undone tasks! Do all the tasks first");
        }

        result.setDone(!result.isDone());
        taskGroupRepository.save(result);
    }
}