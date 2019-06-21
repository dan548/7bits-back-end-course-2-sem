package it.sevenbits.spring_homework.web.service;

import it.sevenbits.spring_homework.config.constant.Regexps;
import it.sevenbits.spring_homework.core.errorcodes.UsersResponseCode;
import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.UsersServiceResponse;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository repository;

    public UsersService(final UsersRepository repository) {
        this.repository = repository;
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

}
