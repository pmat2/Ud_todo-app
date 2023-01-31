package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.model.ProjectStep;
import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.model.projection.GroupReadModel;
import io.github.pmat.todoapp.model.projection.GroupWriteModel;
import io.github.pmat.todoapp.model.projection.ProjectWriteModel;
import io.github.pmat.todoapp.model.projection.TaskWriteModel;
import io.github.pmat.todoapp.repository.TaskRepository;
import io.github.pmat.todoapp.service.TaskGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/group")
@IllegalExceptionProcessing
public class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService service;
    private final TaskRepository repository;

    public TaskGroupController(final TaskGroupService taskGroupService, final TaskRepository repository) {
        this.service = taskGroupService;
        this.repository = repository;
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GroupReadModel>> getTaskGroups() {
        logger.info("[getTaskGroups] - returning all groups");
        return ResponseEntity.ok(service.readAll());
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel source) {
        logger.info("[createGroup] - create group for: {}", source);
        GroupReadModel result = service.createGroup(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @ResponseBody
    @Transactional
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> toggleGroup(@PathVariable Integer id) {
        logger.info("[toggleGroup] - toggle group with id: {}", id);
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Task>> getTasksFromGroup(@PathVariable Integer id) {
        logger.info("[getTasksFromGroup] - for group id: {}", id);
        return ResponseEntity.ok(repository.findAllByGroupId(id));
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String showGroups(Model model) {
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("group") GroupWriteModel current) {
        logger.info("[addGroupTask] - adding task to view");
        current.getTasks().add(new TaskWriteModel());
        return "groups";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    String addGroup(@ModelAttribute("group") @Valid GroupWriteModel current,
                      BindingResult bindingResult,
                      Model model) {
        logger.info("[addGroup] - adding group from groups view");
        if (bindingResult.hasErrors()) {
            logger.info("[addGroup] - validation failed, errors: {}", bindingResult.getAllErrors());
            return "groups";
        }
        service.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Group added");
        return "groups";
    }

    @ModelAttribute("groups")
    public List<GroupReadModel> getGroups() {
        return service.readAll();
    }
}
