package cz.radeknolc.stagger.model.payload;

public enum ServerResponseMessage {

    /*
     AUTHENTICATION
     */
    AUTH_ACCOUNT_DISABLED,
    AUTH_BAD_CREDENTIALS,
    AUTH_ACCOUNT_EXPIRED,
    AUTH_ACCOUNT_LOCKED,
    AUTH_CREDENTIALS_EXPIRED,

    /*
     VALIDATION
     */
    VALIDATION_ERROR,

    /*
     UNIVERSITY
     */
    UNIVERSITY_CREATED,
    UNIVERSITY_ASSIGNED,
    UNIVERSITY_NOT_ASSIGNED,
    UNIVERSITY_DISMISSED,
    UNIVERSITY_NOT_DISMISSED,

    /*
    USER
     */
    USER_REGISTRATION_SUCCESS,
}
