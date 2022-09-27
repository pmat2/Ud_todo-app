package io.github.pmat.todoapp.service;

import io.github.pmat.todoapp.config.TaskConfigurationProperties;
import io.github.pmat.todoapp.repository.ProjectRepository;
import io.github.pmat.todoapp.repository.TaskGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and undone group exists")
    void createGroup_allowNoMultipleGroups_and_ExistsOtherUndoneGroup_throw_ISE() {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.isAllowMultipleTasksFromTemplate()).thenReturn(false);

        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);

        Assertions.assertThatIllegalStateException()
                .isThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0));
    }

    @Test
    @DisplayName("should throw ISE when configuration is ok and no projects for given id")
    void createGroup_configOK_and_noProjects_throwsISE(){
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.isAllowMultipleTasksFromTemplate()).thenReturn(true);

        var toTest = new ProjectService(mockRepository, null, mockConfig);

        var exception = Assertions.catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        Assertions.assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");
    }
}