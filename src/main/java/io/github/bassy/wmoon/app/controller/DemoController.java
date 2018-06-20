package io.github.bassy.wmoon.app.controller;

import io.github.bassy.wmoon.app.model.User;
import io.github.bassy.wmoon.app.service.DemoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DemoController {

    private final DemoService demoService;

	public DemoController(DemoService demoService) {
	    this.demoService = demoService;
	}

	@GetMapping("/")
	public String index() {
		return "hoge";
	}

	@GetMapping("/demo")
	public String demoIndex() {
		return "demo";
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute User user, Model mode) {
		demoService.saveUser(user);
		return "redirect:demo";
	}

	@GetMapping("/show")
	public String showUsers(Model model) {
		List<User> users = demoService.selectUsers();
		model.addAttribute("users", users);
		return "demoUsers";
	}

	@GetMapping("/show/user/{id}")
	public String showUserById(@PathVariable int id,  Model model) {
		User user = demoService.selectUser(id);
		model.addAttribute("user", user);
		return "demoUser";
	}
}
