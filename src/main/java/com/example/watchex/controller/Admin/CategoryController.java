package com.example.watchex.controller.Admin;

import com.example.watchex.domain.Category;
import com.example.watchex.service.CategoryService;
import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class CategoryController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin/categories")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 0;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        return "admin/categories/index";
    }

    private Model findPaginate(int page, Model model) {
        Page<Category> categories = categoryService.get(page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("totalItems", categories.getTotalElements());
        model.addAttribute("categories", categories);
        model.addAttribute("models", "categories");
        model.addAttribute("title", "Categories Management");
        return model;
    }

    @GetMapping("/admin/category/create")
    public String create(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/create";
    }

    @PostMapping("/admin/category/save")
    public String save(Category category, RedirectAttributes ra) {
        final Slugify slg = Slugify.builder().build();
        final String slug = slg.slugify(category.getName());
        category.setSlug(slug);
        categoryService.save(category);
        ra.addFlashAttribute("message", messageSource.getMessage("create_category_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/category/edit/{id}")
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

    @PostMapping("/admin/category/update")
    public String update(Category category, RedirectAttributes ra) {
        categoryService.save(category);
        ra.addFlashAttribute("message", messageSource.getMessage("update_category_success", new Object[0], LocaleContextHolder.getLocale()));
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/category/delete/{id}")
    public String save(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            Category category = categoryService.show(id);
            ra.addFlashAttribute("message", messageSource.getMessage("delete_category_success", new Object[0], LocaleContextHolder.getLocale()));
            categoryService.delete(id);
            return "redirect:/admin/categories";
        } catch (ClassNotFoundException exception) {
            ra.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/categories";
        }
    }


}
