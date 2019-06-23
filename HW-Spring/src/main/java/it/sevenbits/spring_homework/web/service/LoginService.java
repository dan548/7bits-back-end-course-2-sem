package it.sevenbits.spring_homework.web.service;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.SignInRequest;
import it.sevenbits.spring_homework.web.model.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class LoginService {

    private final UsersRepository users;
    private final PasswordEncoder passwordEncoder;

    /**
     *
     * @param users repository
     * @param passwordEncoder encodes password to database representation
     */
    public LoginService(final UsersRepository users, final PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param signInRequest login request
     * @return user
     */
    public User login(final SignInRequest signInRequest) {
        User user = users.findByUserName(signInRequest.getLogin());

        if (user == null) {
            throw new LoginFailedException(
                    "User '" + signInRequest.getLogin() + "' not found"
            );
        }

        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new LoginFailedException("Wrong password");
        }

        return new User(user.getId(), user.getUsername(), user.getAuthorities());
    }
}
