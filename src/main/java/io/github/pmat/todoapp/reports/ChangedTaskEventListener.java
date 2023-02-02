package io.github.pmat.todoapp.reports;

import io.github.pmat.todoapp.event.TaskDone;
import io.github.pmat.todoapp.event.TaskEvent;
import io.github.pmat.todoapp.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    private final PersistedTaskEventRepository repository;

    public ChangedTaskEventListener(PersistedTaskEventRepository repository) {
        this.repository = repository;
    }

    @Async
    @EventListener
    void on(TaskDone event) {
        onChanged(event);
    }

    @Async
    @EventListener
    void on(TaskUndone event) {
        onChanged(event);
    }

    private void onChanged(TaskEvent event) {
        logger.info("Event: {}", event);
        repository.save(new PersistedTaskEvent(event));
    }
}
