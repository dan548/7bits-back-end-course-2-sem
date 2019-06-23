package it.sevenbits.spring_homework.web.model.users;

import it.sevenbits.spring_homework.core.errorcodes.UsersResponseCode;

/**
 * User manipulating response.
 */
public class UsersServiceResponse {

    private final UsersResponseCode code;

    /**
     * Constructs a response by response code.
     * @param code repository answer
     */
    public UsersServiceResponse(final UsersResponseCode code) {
        this.code = code;
    }

    public UsersResponseCode getCode() {
        return code;
    }
}
