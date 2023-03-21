package com.productivity_helper.app.repo;

import com.productivity_helper.app.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {

    List<ToDoItem> findToDoItemByUserId(Long userId);

    void markToDoItemAsComplete(ToDoItem item);

    void markToDoItemAsUnDone(ToDoItem item);

    void updateToDoItemWithId(Long id, ToDoItem updatedToDoItem);

    Boolean removeToDoItemById(Long id);

}
