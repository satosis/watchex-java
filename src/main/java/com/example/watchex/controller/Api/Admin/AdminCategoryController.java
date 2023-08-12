package com.example.watchex.controller.Api.Admin;

import com.example.watchex.entity.Category;
import com.example.watchex.exceptions.MessageEntity;
import com.example.watchex.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/api/admin/")
public class AdminCategoryController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("categories")
    public ResponseEntity<?> getListAdv(@RequestParam Map<String, String> params) {
        int page = 0;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        Page<Category> categories = categoryService.get(page);
        return new ResponseEntity<>(new MessageEntity(200, categories), HttpStatus.OK);
    }

    @GetMapping("category/create")
    public String create(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/create";
    }

    @PostMapping("category/save")
    public String save(Category category, RedirectAttributes ra) {
        categoryService.save(category);
        ra.addFlashAttribute("message", messageSource.getMessage("create_category_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/categories";
    }

    @GetMapping("category/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Category category = categoryService.show(id);
            model.addAttribute("category", category);
            return "admin/categories/edit";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/categories";
        }
    }

    @PostMapping("category/update")
    public String update(Category category, RedirectAttributes ra) {
        categoryService.save(category);
        ra.addFlashAttribute("message", messageSource.getMessage("update_category_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/categories";
    }

    @GetMapping("category/delete/{id}")
    public String save(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            Category category = categoryService.show(id);
            ra.addFlashAttribute("message", messageSource.getMessage("delete_category_success", new Object[0], LocaleContextHolder.getLocale()));
            categoryService.delete(id);
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/admin/categories";
    }


}
