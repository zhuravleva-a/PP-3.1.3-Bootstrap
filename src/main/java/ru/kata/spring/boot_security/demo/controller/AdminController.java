package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String showAllUsers(ModelMap model, Authentication authentication) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentUser", authentication.getPrincipal());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "admin/index";
    }

    @GetMapping("/{id}")
    public String showUserById(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/index";
    }

    @GetMapping("/new")
    public String newUser(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/index";
    }

    @PostMapping()
    public String createUser(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/index";
        }

        Set<Role> roles = new HashSet<>();

        for (Role role: user.getRoles()) {
            roles.add(roleService.getRoleByName(role.getName()));
        }
        user.setRoles(roles);

        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@ModelAttribute("user") User user, ModelMap model, @PathVariable("id") int id) {

        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/index";
    }

    @PostMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @PathVariable("id") int id,
                             @RequestParam(name="roles", required = false) String[] roles) {
        if (bindingResult.hasErrors()) {
            return "admin/index";
        }

        Set<Role> roles1 = new HashSet<>();

        if(roles == null) {
            user.setRoles(userService.getUserById(id).getRoles());
        } else {
            for (String role: roles) {
                roles1.add(roleService.getRoleByName(role));
                user.setRoles(roles1);
            }
        }

        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
