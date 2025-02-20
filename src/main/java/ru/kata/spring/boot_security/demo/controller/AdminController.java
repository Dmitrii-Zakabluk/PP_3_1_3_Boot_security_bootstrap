package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "all_users";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("showModal", true);
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRoles());
        return "user_info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user_info";
        }
        try {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User saved successfully");
            return "redirect:/admin/";
        } catch (EntityExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/admin/addNewUser";
        }
    }

    @GetMapping("/updateInfo")
    public String getUpdateUserForm(@RequestParam("userId") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUser(id);
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRoles());
            return "user_info";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/";
        }
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user_info"; // Вернуть на форму редактирования с ошибками
        }
        try {
            userService.updateUser(user); // Предполагается, что у вас есть метод updateUser в userService
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
            return "redirect:/admin/"; // Перенаправление на список пользователей
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/";
        }
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (!userService.existsById(id)) {
                throw new EntityNotFoundException("User with ID " + id + " not found.");
            }
            userService.deleteUserById(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/";
    }

}