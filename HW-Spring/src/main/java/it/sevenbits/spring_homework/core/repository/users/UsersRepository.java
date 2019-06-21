package it.sevenbits.spring_homework.core.repository.users;

import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;

import java.util.List;

public interface UsersRepository {

    User findByUserName(String username);

    List<User> findAll();

    User findById(String id);

    int editUserById(String id, UpdateUserRequest request);

    int addUser(SignUpRequest request);

    boolean checkUsername(String username);

}
