package com.productivity_helper.app.repo;

import com.productivity_helper.app.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {

    List<ToDoItem> findToDoItemByUserId(Long userId);
    void markToDoItemAsComplete(ToDoItem item);
    void markToDoItemAsUnDone(ToDoItem item);

}
