package cz.radeknolc.stagger.model.response;

public enum ServerResponseMessage {

    /*
     GENERAL
    */
    GENERAL_OK,
    GENERAL_SUCCESS,
    GENERAL_ERROR,

    /*
     AUTHENTICATION
     */
    AUTH_ACCOUNT_DISABLED, // Disabled account
    AUTH_BAD_CREDENTIALS, // Wrong credentials
    AUTH_ACCOUNT_EXPIRED, // Expired account
    AUTH_ACCOUNT_LOCKED, // Locked account
    AUTH_CREDENTIALS_EXPIRED, // Expired credentials
    AUTH_ACCESS_DENIED, // No privileges
    AUTH_TOKEN_INVALID, // Invalid token from verification
    AUTH_USER_VERIFY_ERROR, // Mismatching ID in request with token in header

    /*
     VALIDATION
     */
    VALIDATION_ERROR, // Error during validation

    /*
     UNIVERSITY
     */
    UNIVERSITY_CREATED, // University created
    UNIVERSITY_ASSIGNED, // University assigned to user
    UNIVERSITY_NOT_ASSIGNED, // University not assigned to user
    UNIVERSITY_DISMISSED, // University dismissed from user
    UNIVERSITY_NOT_DISMISSED, // University not dismissed from user

    /*
    USER
     */
    USER_REGISTRATION_SUCCESS, // User registration success

    /*
    NOTIFICATION
     */
    NOTIFICATION_READ, // Has been notification marked as read
    NOTIFICATION_NOT_READ, // Has not been notification marked as read
}
