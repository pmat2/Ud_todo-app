package io.github.pmat.todoapp.service;

import io.github.pmat.todoapp.config.TaskConfigurationProperties;
import io.github.pmat.todoapp.repository.ProjectRepository;
import io.github.pmat.todoapp.repository.TaskGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and undone group exists")
    void createGroup_noAllowMultipleTasks_And_notExistsByDoneIsFalseAndProjectID() {
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        TaskConfigurationProperties mockConfig = configMultipleTasksReturning(false);
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);

        assertThatIllegalStateException()
                .isThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0));
    }

    @Test
    @DisplayName("should throw IAE when configuration is ok and no projects for given id")
    void createGroup_allowMultipleTasks_And_noProjects_throwsIAE(){
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mockConfig = configMultipleTasksReturning(true);

        var toTest = new ProjectService(mockRepository, null, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");
    }

    @Test
    @DisplayName("should throw IAE when configured to allow just one group and no groups and projects for given id")
    void createGroup_noAllowMultipleTasks_And_noUndoneGroupExists_throwsIAE(){
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mockConfig = configMultipleTasksReturning(false);
        TaskGroupRepository groupRepository = groupRepositoryReturning(false);

        var toTest = new ProjectService(mockRepository, groupRepository, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    private TaskConfigurationProperties configMultipleTasksReturning(final boolean result){
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.isAllowMultipleTasksFromTemplate()).thenReturn(result);

        return mockConfig;
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result){
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);

        return mockGroupRepository;
    }


}