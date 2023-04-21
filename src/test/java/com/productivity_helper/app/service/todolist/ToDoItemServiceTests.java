package com.productivity_helper.app.service.todolist;

import com.productivity_helper.app.model.todolist.ToDoItem;
import com.productivity_helper.app.model.todolist.ToDoItemDto;
import com.productivity_helper.app.model.user.AppUser;
import com.productivity_helper.app.repo.todolist.ToDoItemRepository;
import com.productivity_helper.app.repo.user.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToDoItemServiceTests {

    @Mock
    private ToDoItemRepository itemRepo;
    @Mock
    private AppUserRepository userRepo;

    private ToDoItemService itemService;

    @BeforeEach
    void setUp() {
        this.itemService = new ToDoItemService(itemRepo, userRepo);
    }

    @Test
    public void canAddTask() {
        Long id = any();
        Optional<AppUser> user = Optional.of(new AppUser());
        ToDoItemDto item = new ToDoItemDto("test", new Date());

        when(userRepo.findById(id)).thenReturn(user);

        Long taskId = itemService.addTask(item, user.get().getId());
        // TODO: repair it
    }

    @Test
    public void canUpdateTask() {
        Long id = any();
        ToDoItemDto updatedTask = new ToDoItemDto("text", new Date());
        Optional<ToDoItem> item = Optional.of(new ToDoItem());

        when(itemRepo.findById(id)).thenReturn(item);

        ToDoItemDto returnedItem = itemService.updateTask(updatedTask, item.get().getId());

        assertThat(returnedItem).isNotNull();
        assertThat(returnedItem.text()).isEqualTo(updatedTask.text());
        assertThat(returnedItem.dueDate()).isEqualTo(updatedTask.dueDate());
    }

    @Test
    public void canMarkTaskAsDone() {
        Long id = any();

        when(itemRepo.findById(id)).thenReturn(Optional.of(new ToDoItem()));

        boolean isDone = itemService.markTaskAsDone(id);

        assertThat(isDone).isTrue();
    }

    @Test
    public void canDeleteTaskAsDone() {
        Long id = any();

        when(itemRepo.findById(id)).thenReturn(Optional.of(new ToDoItem()));

        Long taskId = itemService.deleteTask(id);

        // TODO: improve it
    }
}
