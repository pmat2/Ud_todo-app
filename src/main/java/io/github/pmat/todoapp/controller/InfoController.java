package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.config.TaskConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {

    private DataSourceProperties dataSource;
    private TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties dataSource,TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    public String url(){
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    public boolean prop(){
        return myProp.isAllowMultipleTasksFromTemplate();
    }
}
