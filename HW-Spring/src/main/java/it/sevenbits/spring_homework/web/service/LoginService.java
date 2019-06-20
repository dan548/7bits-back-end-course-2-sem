package it.sevenbits.spring_homework.web.service;

import it.sevenbits.spring_homework.core.repository.users.UsersRepository;
import it.sevenbits.spring_homework.web.model.users.Login;
import it.sevenbits.spring_homework.web.model.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UsersRepository users;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UsersRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(Login login) {
        User user = users.findByUserName(login.getLogin());

        if (user == null) {
            throw new LoginFailedException(
                    "User '" + login.getLogin() + "' not found"
            );
        }

        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new LoginFailedException("Wrong password");
        }

        return new User(user.getId(), user.getUsername(), user.getAuthorities());
    }
}
