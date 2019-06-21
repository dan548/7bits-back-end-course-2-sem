package it.sevenbits.spring_homework.web.service;

import it.sevenbits.spring_homework.config.constant.Regexps;
import it.sevenbits.spring_homework.core.errorcodes.UsersResponseCode;
import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.UsersServiceResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(final UsersRepository repository, final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

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

    public UsersServiceResponse addUser(final SignUpRequest request) {
        if (request != null && request.getUsername() != null && request.getPassword() != null &&
        !request.getUsername().equals("") && !request.getPassword().equals("")) {
            if (!repository.checkUsername(request.getUsername())) {
                String password = passwordEncoder.encode(request.getPassword());
                repository.addUser(new SignUpRequest(request.getUsername(), password));
                return new UsersServiceResponse(UsersResponseCode.SUCCESS);
            } else {
                return new UsersServiceResponse(UsersResponseCode.EXISTS);
            }
        }
        return new UsersServiceResponse(UsersResponseCode.INVALID);
    }

}
