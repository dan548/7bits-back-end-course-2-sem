package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UsersServiceResponse;
import it.sevenbits.spring_homework.web.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Account creating controller.
 */
@Controller
@RequestMapping("/signup")
public class SignUpController {

    private final UsersService service;

    /**
     * Constructs sign up controller.
     * @param service service that contacts with users repository
     */
    public SignUpController(final UsersService service) {
        this.service = service;
    }

    /**
     * Gets server response to sign up request.
     * @param request sign up request
     * @return server response
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity signUpUser(final @RequestBody SignUpRequest request) {
        UsersServiceResponse response = service.addUser(request);
        switch (response.getCode()) {
            case SUCCESS:
                return ResponseEntity.noContent().build();
            case EXISTS:
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
                default:
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
