package it.sevenbits.spring_homework.web.service;

import it.sevenbits.spring_homework.config.constant.Regexps;
import it.sevenbits.spring_homework.core.errorcodes.UsersResponseCode;
import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import it.sevenbits.spring_homework.web.model.users.UsersServiceResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User manipulating service.
 */
@Service
public class UsersService {

    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs service.
     * @param repository user repository
     * @param passwordEncoder encodes passwords to store in database
     */
    public UsersService(final UsersRepository repository, final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Edits users by the given id.
     * @param id user id
     * @param request user updating request
     * @return response for controller
     */
    public UsersServiceResponse editUserById(final String id, final UpdateUserRequest request) {
        if (id != null && id.matches(Regexps.UUID)) {
            if (request == null || request.getStatus() == null && request.getAuthorities() == null) {
                return new UsersServiceResponse(UsersResponseCode.INVALID);
            }
            int rows = repository.editUserById(id, request);
            if (rows == 0) {
                return new UsersServiceResponse(UsersResponseCode.NOT_FOUND);
            } else {
                return new UsersServiceResponse(UsersResponseCode.SUCCESS);
            }
        }
        return new UsersServiceResponse(UsersResponseCode.INVALID);
    }

    /**
     * Handles user adding requests.
     * @param request user signing up request
     * @return service response
     */
    public UsersServiceResponse addUser(final SignUpRequest request) {
        if (request != null && request.getUsername() != null && request.getPassword() != null &&
        !request.getUsername().equals("") && !request.getPassword().equals("")) {
            if (!repository.checkUsername(request.getUsername())) {
                String password = passwordEncoder.encode(request.getPassword());
                if (repository.addUser(new SignUpRequest(request.getUsername(), password)) > 0) {
                    return new UsersServiceResponse(UsersResponseCode.SUCCESS);
                }
            } else {
                return new UsersServiceResponse(UsersResponseCode.EXISTS);
            }
        }
        return new UsersServiceResponse(UsersResponseCode.INVALID);
    }

    /**
     * Gets all users
     * @return user list
     */
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * Finds user by id
     * @param id user id
     * @return user
     */
    public User findById(final String id) {
        return repository.findById(id);
    }

}
