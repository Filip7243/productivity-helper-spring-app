package com.productivity_helper.app.repo.todolist;

import com.productivity_helper.app.model.todolist.ToDoItem;
import com.productivity_helper.app.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {

    List<ToDoItem> findByUser(AppUser user);
    @Query("SELECT i FROM ToDoItem i WHERE i.isDone = false AND i.user = :user")
    List<ToDoItem> findAllNotDoneYetByUser(AppUser user);
}
