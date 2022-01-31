package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class DBInit {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public DBInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void createUsersWithRoles() {

        Role role1 = new Role("ADMIN");
        Role role2 = new Role("USER");
        Role role3 = new Role("STUDENT");

        roleService.save(role1);
        roleService.save(role2);
        roleService.save(role3);

        User user1 = new User("Александр", "Стретович", 35, "andrey@email.com");
        User user2 = new User("Максим", "Завьялов", 28, "maksim@email.com");
        User user3 = new User("Сабина", "Хасанова", 37, "sabina@email.com");
        User user4 = new User("Полина", "Соколова", 32, "polina@email.com");
        User user5 = new User("Антон", "Константинов", 29, "anton@email.com");

        user1.setRoles(new HashSet<>(Set.of(role1,role2)));
        user2.setRoles(new HashSet<>(Set.of(role1)));
        user3.setRoles(new HashSet<>(Set.of(role2)));
        user4.setRoles(new HashSet<>(Set.of(role3)));


        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);
        userService.save(user5);




    }

}