package org.zcorp.java2.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.zcorp.java2.to.UserTo;
import org.zcorp.java2.web.user.AbstractUserController;

import javax.validation.Valid;

@Controller
public class RootController extends AbstractUserController {
    @GetMapping("/")
    public String root() {
        return "redirect:meals";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

//    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @GetMapping("/meals")
    public String meals() {
        return "meals";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "profile";
        }
        super.update(userTo, SecurityUtil.authUserId());
        SecurityUtil.get().update(userTo);
        status.setComplete();
        return "redirect:meals";
    }
}
