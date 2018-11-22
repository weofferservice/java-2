package org.zcorp.java2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.zcorp.java2.AuthorizedUser;
import org.zcorp.java2.to.UserTo;
import org.zcorp.java2.util.UserUtil;
import org.zcorp.java2.web.user.AbstractUserController;
import org.zcorp.java2.web.validator.user.UserToValidator;

import javax.validation.Valid;

@Controller
public class RootController extends AbstractUserController {
    @Autowired
    private UserToValidator userToValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(userToValidator);
    }

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
    public String profile(ModelMap model, @AuthenticationPrincipal AuthorizedUser authUser) {
        model.addAttribute("userTo", authUser.getUserTo());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status, @AuthenticationPrincipal AuthorizedUser authUser) {
        if (result.hasErrors()) {
            return "profile";
        }
        super.update(userTo, authUser.getId());
        authUser.update(userTo);
        status.setComplete();
        return "redirect:meals";
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", UserUtil.createEmptyTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        }
        super.create(UserUtil.createFromTo(userTo));
        status.setComplete();
        return "redirect:login?message=app.registered&username=" + userTo.getEmail().toLowerCase();
    }
}
