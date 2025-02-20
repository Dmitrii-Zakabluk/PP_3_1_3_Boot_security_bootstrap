package ru.kata.spring.boot_security.demo.db_users_init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class DBUsersInit {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DBUsersInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");

        roleService.addRole(admin);
        roleService.addRole(user);

        userService.saveUser(new User("ivan", "ivan", "Иван", "Иванов", 31, "i.ivanov@mail.ru", Set.of(admin)));
        userService.saveUser(new User("alex", "alex", "Александр", "Кузнецов", 18, "ak@mail.ru", Set.of(user)));
        userService.saveUser(new User("svet", "svet", "Cветлана", "Иванова", 43, "sv.admin@mail.ru", Set.of(admin)));
        userService.saveUser(new User("pavel", "pavel", "Павел", "Петров", 36, "user@mail.ru", Set.of(user)));
    }
}
