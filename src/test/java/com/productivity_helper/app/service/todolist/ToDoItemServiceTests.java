package com.productivity_helper.app.service.todolist;

import com.productivity_helper.app.model.user.AppUser;
import com.productivity_helper.app.repo.todolist.ToDoItemRepository;
import com.productivity_helper.app.repo.user.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
        when(userRepo.findById(any())).thenReturn(Optional.of(new AppUser()));
    }
}
