package it.sevenbits.spring_homework.web.model.users;

import it.sevenbits.spring_homework.core.errorcodes.UsersResponseCode;

public class UsersServiceResponse {

    private final UsersResponseCode code;

    public UsersServiceResponse(UsersResponseCode code) {
        this.code = code;
    }

    public UsersResponseCode getCode() {
        return code;
    }
}
