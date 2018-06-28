package io.github.bassy.wmoon.notest.controller;

import io.github.bassy.wmoon.notest.model.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoTestController {

    public NoTestController() {
    }

    @GetMapping("/notest/")
    public String notestIndex() {
        return "notest/index";
    }

    @GetMapping("/notest/input")
    public String inputFormArticle() {
        return "notest/article";
    }

    @PostMapping("/notest/article")
    public String postArticle(@ModelAttribute Article article, Model mode) {
        return "notest/articles";
    }

    @GetMapping("/notest/articles")
    public String showArticles() {
        return "notest/articles";
    }
}
