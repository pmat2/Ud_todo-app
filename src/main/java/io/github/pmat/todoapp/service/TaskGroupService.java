package io.github.pmat.todoapp.service;

import io.github.pmat.todoapp.config.TaskConfigurationProperties;
import io.github.pmat.todoapp.model.TaskGroup;
import io.github.pmat.todoapp.model.projection.GroupReadModel;
import io.github.pmat.todoapp.model.projection.GroupWriteModel;
import io.github.pmat.todoapp.repository.TaskGroupRepository;
import io.github.pmat.todoapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@Service
public class TaskGroupService {
    private TaskGroupRepository taskGroupRepository;
    private TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.taskGroupRepository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source){
        TaskGroup result = taskGroupRepository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return taskGroupRepository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId){
        if(taskRepository.existsByDoneIsFalseAndGroupId(groupId)){
            throw new IllegalStateException("Group has undone tasks! Do all the tasks first");
        }
        TaskGroup result = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Task group with given ID not found"));
        result.setDone(!result.getDone());
        taskGroupRepository.save(result);
    }
}
