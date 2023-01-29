package io.github.pmat.todoapp.model.projection;

import io.github.pmat.todoapp.model.Task;
import io.github.pmat.todoapp.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {

    @Test
    @DisplayName("GIVEN null deadline task WHEN create group THEN should create null deadline group")
    void constructorNoDeadlineCreatesNullDeadline(){
        // given
        var source = new TaskGroup();
        source.setDescription("test desc");
        source.setTasks(Set.of(new Task("test task", null)));

        // when
        var restult = new GroupReadModel(source);

        // then
        assertThat(restult).hasFieldOrPropertyWithValue("deadline", null);
    }
}