package io.github.pmat.todoapp.controller;

import io.github.pmat.todoapp.config.TaskConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {

    private DataSourceProperties dataSource;
    private TaskConfigurationProperties myProp;
    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    public InfoController(DataSourceProperties dataSource, TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping
    public DataSourceProperties dataSourceProperties() {
        logger.info("[dataSourceProperties] invoked");
        return dataSource;
    }

    @GetMapping("/url")
    public String url() {
        logger.info("[url] invoked");
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    public boolean prop() {
        logger.info("[prop] invoked");
        return myProp.isAllowMultipleTasksFromTemplate();
    }
}
