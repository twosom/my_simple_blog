package com.icloud.my_portfolio.exception;

public class UserEmailDuplicateException extends RuntimeException{
    public UserEmailDuplicateException() {
        super();
    }

    public UserEmailDuplicateException(String message) {
        super(message);
    }

    public UserEmailDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEmailDuplicateException(Throwable cause) {
        super(cause);
    }
}
