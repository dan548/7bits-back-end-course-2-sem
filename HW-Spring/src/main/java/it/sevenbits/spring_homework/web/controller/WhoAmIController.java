package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * "Who-am-I" page controller.
 */
@Controller
@RequestMapping("/whoami")
public class WhoAmIController {

    private final UsersRepository repo;

    /**
     * Constructs controller from repository.
     * @param repo user repository
     */
    public WhoAmIController(final UsersRepository repo) {
        this.repo = repo;
    }

    /**
     * Gets current authenticated user.
     * @return user
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<User> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User(authentication);
        if (user.getId() != null) {
            user = repo.findById(user.getId());
        } else {
            user = repo.findByUserName(user.getUsername());
        }
        return ResponseEntity.ok(user);
    }

}
