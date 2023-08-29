package com.example.watchex.controller.Api.Admin;

import com.example.watchex.entity.User;
import com.example.watchex.exceptions.MessageEntity;
import com.example.watchex.service.UserService;
import com.example.watchex.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users/")
@RequiredArgsConstructor
public class AdminUserController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getListAdv(@RequestParam Map<String, String> params) {
        int page = 0;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        Page<User> users = userService.get(page);
        return new ResponseEntity<>(new MessageEntity(200, users), HttpStatus.OK);
    }

    @GetMapping("profile")
    public ResponseEntity<?> profile() {
        User auth = CommonUtils.getCurrentUser();
        return ResponseEntity.ok().body(auth);
    }

    @GetMapping("profile/image")
    public ResponseEntity<byte[]> displayImage() throws SQLException
    {
        User auth = CommonUtils.getCurrentUser();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(auth.getAvatar());
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
