package com.productivity_helper.app.repo;

import com.productivity_helper.app.model.ToDoItem;

import java.util.List;

public interface ToDoItemRepository {

    void addToDoItem(ToDoItem toDoItem);
    boolean removeToDoItem(Long id);
    boolean modifyToDoItem(Long oldToDoItemId, ToDoItem newToDoItem);
    List<ToDoItem> findUserToDoItems(Long userId);
    void markToDoItemAsComplete(ToDoItem item);
    void markToDoItemAsUnDone(ToDoItem item);

}
