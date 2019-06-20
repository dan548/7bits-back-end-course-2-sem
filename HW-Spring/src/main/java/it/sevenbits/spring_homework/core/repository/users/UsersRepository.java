package it.sevenbits.spring_homework.core.repository.users;

import it.sevenbits.spring_homework.web.model.users.User;

import java.util.List;

public interface UsersRepository {

    User findByUserName(String username);

    List<User> findAll();

    User findById(String id);

}
