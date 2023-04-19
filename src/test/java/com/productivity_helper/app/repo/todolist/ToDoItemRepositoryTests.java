package com.productivity_helper.app.repo.todolist;

import com.productivity_helper.app.model.todolist.ToDoItem;
import com.productivity_helper.app.model.user.AppUser;
import com.productivity_helper.app.model.user.AppUserRole;
import com.productivity_helper.app.repo.user.AppUserRepository;
import com.productivity_helper.app.repo.user.AppUserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ToDoItemRepositoryTests {


    @Autowired
    private ToDoItemRepository itemRepo;
    @Autowired
    private AppUserRepository userRepo;
    @Autowired
    private AppUserRoleRepository roleRepo;

    @BeforeEach
    void setUp() {
        AppUserRole admin = new AppUserRole("ADMIN");
        roleRepo.save(admin);

        AppUser user = new AppUser("John", "Doe", "john@mail.com", "pw123");
        AppUser user1 = new AppUser("Mike", "Doe", "mike@mail.com", "pw123");
        user.addRoleToUser(admin);
        user1.addRoleToUser(admin);
        userRepo.save(user);
        userRepo.save(user1);

        ToDoItem i1 = new ToDoItem("RUNNING", new Date(), user);
        ToDoItem i2 = new ToDoItem("MAKE A DINNER", new Date(), user);
        ToDoItem i3 = new ToDoItem("WORK", new Date(), user);
        ToDoItem i3Done = new ToDoItem(null, "WORK", new Date(), true, user);
        ToDoItem i4 = new ToDoItem("WORK", new Date(), user1);
        ToDoItem i5 = new ToDoItem("WORK", new Date(), user1);

        itemRepo.save(i1);
        itemRepo.save(i2);
        itemRepo.save(i3);
        itemRepo.save(i4);
        itemRepo.save(i5);
    }

    @Test
    public void canFindToDoItemsByUser() {
        AppUser user = userRepo.findByEmail("john@mail.com").orElseThrow();

        List<ToDoItem> todoItems = itemRepo.findByUser(user);

        assertThat(todoItems).isNotNull();
        assertThat(todoItems.size()).isEqualTo(3);
    }

    @Test
    public void canFindNotDoneTasksByUser() {
        AppUser user = userRepo.findByEmail("john@mail.com").orElseThrow();

        List<ToDoItem> notDone = itemRepo.findAllNotDoneYetByUser(user);

        assertThat(notDone).isNotNull();
        assertThat(notDone.size()).isEqualTo(3);
    }

}
