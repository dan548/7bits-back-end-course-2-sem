package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import it.sevenbits.spring_homework.web.model.users.UsersServiceResponse;
import it.sevenbits.spring_homework.web.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersRepository usersRepository;
    private final UsersService service;

    public UsersController(final UsersRepository usersRepository, final UsersService service) {
        this.usersRepository = usersRepository;
        this.service = service;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserInfo(final @PathVariable("id") String id) {
        return Optional
                .ofNullable(usersRepository.findById(id))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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
