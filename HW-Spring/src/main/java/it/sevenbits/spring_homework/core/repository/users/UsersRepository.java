package it.sevenbits.spring_homework.core.repository.users;

import it.sevenbits.spring_homework.web.model.users.SignUpRequest;
import it.sevenbits.spring_homework.web.model.users.UpdateUserRequest;
import it.sevenbits.spring_homework.web.model.users.User;

import java.util.List;

/**
 * User repository interface
 */
public interface UsersRepository {

    /**
     * Gets user by name
     * @param username login
     * @return user
     */
    User findByUserName(String username);

    /**
     * Gets all users
     * @return list of all users
     */
    List<User> findAll();

    /**
     * Gets user by id
     * @param id user id
     * @return user
     */
    User findById(String id);

    /**
     * Edits user by id
     * @param id user id
     * @param request updating request
     * @return number of rows changed
     */
    int editUserById(String id, UpdateUserRequest request);

    /**
     * Adds user
     * @param request user sign up request
     * @return number of rows added
     */
    int addUser(SignUpRequest request);

    /**
     * Checks if username exists
     * @param username username to check
     * @return true if username exists
     */
    boolean checkUsername(String username);

}
