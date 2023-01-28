package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.model.Project;
import io.github.pmat.todoapp.model.ProjectStep;
import io.github.pmat.todoapp.model.projection.ProjectWriteModel;
import io.github.pmat.todoapp.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    public static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model) {
        logger.info("[showProjects] - returning projects view");
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        logger.info("[addProjectStep] - adding project step to view");
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute("project") @Valid ProjectWriteModel current,
                      BindingResult bindingResult,
                      Model model) {
        logger.info("[addProject] - adding project from projects view");
        if (bindingResult.hasErrors()) {
            logger.info("[addProject] - validation failed, errors: {}", bindingResult.getAllErrors());
            return "projects";
        }
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Project added");
        return "projects";
    }

    @PostMapping("/{id}")
    String createGroup(@ModelAttribute("project") ProjectWriteModel current,
                       Model model,
                       @PathVariable int id,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline) {
        logger.info("[createGroup] - with param id: {}", id);
        try {
            service.createGroup(deadline, id);
            model.addAttribute("message", "Group added");
        } catch (IllegalStateException | IllegalArgumentException e) {
            logger.error("[createGroup] - with param id: {}, errors while creating group", id, e);
            model.addAttribute("message", "Error while creating group");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    public List<Project> getProjects() {
        return service.readAll();
    }
}
