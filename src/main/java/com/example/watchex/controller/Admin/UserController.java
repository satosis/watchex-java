package com.example.watchex.controller.Admin;

import com.example.watchex.entity.User;
import com.example.watchex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.SecureRandom;
import java.util.Map;

@Controller
@RequestMapping("/admin/")
public class UserController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @GetMapping("users")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 0;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        return "admin/users/index";
    }

    private Model findPaginate(int page, Model model) {
        Page<User> users = userService.get(page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("totalItems", users.getTotalElements());
        model.addAttribute("users", users);
        model.addAttribute("models", "users");
        model.addAttribute("title", "Users Management");
        return model;
    }

    @GetMapping("user/create")
    public String create(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/create";
    }

    @PostMapping("user/save")
    public String save(User user, RedirectAttributes ra) {
        int strength = 10; // work factor of bcrypt

        user.setIsAdmin(true);
        userService.save(user);
        ra.addFlashAttribute("message", messageSource.getMessage("create_user_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/users";
    }

    @GetMapping("user/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            User user = userService.show(id);
            model.addAttribute("user", user);
            return "admin/users/edit";
        } catch (UserPrincipalNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/users";
        }
    }

    @PostMapping("user/update")
    public String update(User user, RedirectAttributes ra) {
        userService.save(user);
        ra.addFlashAttribute("message", messageSource.getMessage("update_user_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/users";
    }

    @GetMapping("user/delete/{id}")
    public String save(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            User user = userService.show(id);
            ra.addFlashAttribute("message", messageSource.getMessage("delete_user_success", new Object[0], LocaleContextHolder.getLocale()));
            userService.delete(id);
            return "redirect:/admin/users";
        } catch (UserPrincipalNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/users";
        }
    }


}
