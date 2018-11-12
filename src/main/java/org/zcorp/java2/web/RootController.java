package org.zcorp.java2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/")
    public String root() {
        return "redirect:meals";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @GetMapping("/meals")
    public String meals() {
        return "meals";
    }
}
