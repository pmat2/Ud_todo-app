package io.github.pmat.todoapp.reports;

import io.github.pmat.todoapp.event.TaskDone;
import io.github.pmat.todoapp.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    @EventListener
    void on(TaskDone event){
        logger.info("Event: {}", event);
    }

    @EventListener
    void on(TaskUndone event){
        logger.info("Event: {}", event);
    }
}
