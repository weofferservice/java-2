package org.zcorp.java2.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zcorp.java2.model.Meal;
import org.zcorp.java2.service.MealService;
import org.zcorp.java2.to.MealWithExceed;
import org.zcorp.java2.util.ValidationUtil;
import org.zcorp.java2.util.exception.ValidationException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/profile/meals";

    @Autowired
    public MealRestController(MealService service) {
        super(service);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@Valid @RequestBody Meal meal, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(ValidationUtil.createErrorResponse(result));
        }

        Meal created = super.create(meal);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Meal meal, BindingResult result, @PathVariable("id") int id) {
        if (result.hasErrors()) {
            throw new ValidationException(ValidationUtil.createErrorResponse(result));
        }
        super.update(meal, id);
    }

    @Override
    @GetMapping("/filter")
    public List<MealWithExceed> getBetween(@RequestParam(required = false) LocalDate startDate,
                                           @RequestParam(required = false) LocalTime startTime,
                                           @RequestParam(required = false) LocalDate endDate,
                                           @RequestParam(required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @Override
    @GetMapping
    public List<MealWithExceed> getAll() {
        return super.getAll();
    }

}