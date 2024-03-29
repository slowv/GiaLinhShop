package com.slowv.fruit.web.rest.admin.impl;

import com.slowv.fruit.domain.Category;
import com.slowv.fruit.service.impl.CategoryServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/admin/category")
public class CategoryResource {

    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping
    public String list(Model model, @RequestParam( value = "page") int page, @RequestParam("size") int size) {
        if (page != 0){
            page = page - 1;
        }

        Page<Category> categories = categoryServiceImpl.findAll(page, size);
        model.addAttribute("pageCategory", categories);
        return "admin/page/category/list";
    }
    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") long id) {
        return "admin/page/category/create";
    }

}
