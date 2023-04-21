package com.productivity_helper.app.service.todolist;

import com.productivity_helper.app.model.todolist.ToDoItem;
import com.productivity_helper.app.model.todolist.ToDoItemDto;
import com.productivity_helper.app.model.user.AppUser;
import com.productivity_helper.app.repo.todolist.ToDoItemRepository;
import com.productivity_helper.app.repo.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToDoItemService {

    private final ToDoItemRepository itemRepo;
    private final AppUserRepository userRepo;

    public Long addTask(ToDoItemDto task, Long userId) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        ToDoItem newTask = new ToDoItem(task.text(), task.dueDate(), user);
        itemRepo.save(newTask);

        return newTask.getId();
    }

    public ToDoItemDto updateTask(ToDoItemDto updatedTask, Long taskId) {
        ToDoItem task = itemRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found!"));

        task.setText(updatedTask.text());
        task.setDueDate(updatedTask.dueDate());

        itemRepo.save(task);

        return new ToDoItemDto(task.getText(), task.getDueDate());
    }

    public boolean markTaskAsDone(Long taskId) {
        ToDoItem task = itemRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found!"));
        task.setIsDone(true);

        itemRepo.save(task);

        return task.getIsDone();
    }

    public Long deleteTask(Long taskId) {
        ToDoItem task = itemRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found!"));

        itemRepo.deleteById(taskId);

        return task.getId();
    }
}
