package io.github.pmat.todoapp;

import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.model.TaskGroup;
import io.github.pmat.todoapp.repository.TaskGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);

    private final TaskGroupRepository repository;

    public Warmup(TaskGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed");
        String description = "ApplicationContextEvent";
        if(!repository.existsByDescription(description)){
            logger.info("No required groups found. Adding new groups");
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextClosedEvent", null, group),
                    new Task("ContextRefreshedEvent", null, group),
                    new Task("ContextStoppedEvent", null, group),
                    new Task("ContextStartedEvent", null, group)
                    ));
            repository.save(group);
        }
    }
}
