package org.zcorp.java2.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zcorp.java2.model.User;
import org.zcorp.java2.to.UserTo;
import org.zcorp.java2.util.ValidationUtil;
import org.zcorp.java2.util.exception.ValidationException;

import javax.validation.Valid;

import static org.zcorp.java2.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(ProfileRestController.REST_URL)
public class ProfileRestController extends AbstractUserController {

    static final String REST_URL = "/rest/profile";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        return super.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authUserId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(ValidationUtil.createErrorResponse(result));
        }
        super.update(userTo, authUserId());
    }

    @GetMapping(value = "/text")
    public String testUTF8() {
        return "Русский текст";
    }

}