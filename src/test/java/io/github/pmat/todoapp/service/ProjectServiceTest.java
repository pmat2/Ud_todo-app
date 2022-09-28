package io.github.pmat.todoapp.service;

import io.github.pmat.todoapp.config.TaskConfigurationProperties;
import io.github.pmat.todoapp.model.Project;
import io.github.pmat.todoapp.model.ProjectStep;
import io.github.pmat.todoapp.model.TaskGroup;
import io.github.pmat.todoapp.model.projection.GroupReadModel;
import io.github.pmat.todoapp.repository.ProjectRepository;
import io.github.pmat.todoapp.repository.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void createGroup_allowMultipleTasks_And_noProjects_throwsIAE() {
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
    void createGroup_noAllowMultipleTasks_And_noUndoneGroupExists_throwsIAE() {
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

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_allowMultipleTasks_And_existProject_createsAndSavesGroup() {
        var today = LocalDate.now().atStartOfDay();
        var project = projectWith("bar", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        int count = inMemoryGroupRepo.count();
        TaskConfigurationProperties mockConfig = configMultipleTasksReturning(true);

        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, mockConfig);
        GroupReadModel result = toTest.createGroup(today, 1);

        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(count + 1).isEqualTo(inMemoryGroupRepo.count());
    }

    private TaskConfigurationProperties configMultipleTasksReturning(final boolean result) {
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.isAllowMultipleTasksFromTemplate()).thenReturn(result);

        return mockConfig;
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);

        return mockGroupRepository;
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getProjectSteps()).thenReturn(steps);
        return result;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private Map<Integer, TaskGroup> map = new HashMap<>();
        int index = 0;

        public int count(){
            return map.size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            entity.setId(++index);
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(g -> !g.getDone())
                    .anyMatch(g -> g.getProject() != null
                            && g.getProject().getId() == projectId);
        }
    }

}