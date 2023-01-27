package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.model.ProjectStep;
import io.github.pmat.todoapp.model.projection.ProjectWriteModel;
import io.github.pmat.todoapp.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model){
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute ProjectWriteModel current, Model model){
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Project added");
        return "projects";
    }
}
