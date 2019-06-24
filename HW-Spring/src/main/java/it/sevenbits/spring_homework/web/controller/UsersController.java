package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import it.sevenbits.spring_homework.web.model.users.UsersServiceResponse;
import it.sevenbits.spring_homework.web.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

/**
 * Controller to handle getting or updating users.
 */
@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersService service;

    /**
     * Constructs controller.
     * @param service service
     */
    public UsersController(final UsersService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Gets user info by id.
     * @param id user id
     * @return user
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserInfo(final @PathVariable("id") String id) {
        return Optional
                .ofNullable(service.findById(id))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates user info by id.
     * @param id user id
     * @param request fields to update
     * @return response code
     */
    @PatchMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<User> updateUser(final @PathVariable("id") String id, final @RequestBody UpdateUserRequest request) {
        UsersServiceResponse response = service.editUserById(id, request);
        switch (response.getCode()) {
            case SUCCESS:
                return ResponseEntity.noContent().build();
            case NOT_FOUND:
                return ResponseEntity.notFound().build();
            case INVALID:
            default:
                    return ResponseEntity.badRequest().build();
        }
    }
}
