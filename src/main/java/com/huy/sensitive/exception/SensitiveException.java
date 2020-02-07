package com.huy.sensitive.exception;

/**
 * 脱敏异常
 *
 * @author: huyong
 * @since: 2020/2/5 21:27
 */
public class SensitiveException extends RuntimeException {

    private static final long serialVersionUID = -9142997017428339129L;

    public SensitiveException() {
    }

    public SensitiveException(String message) {
        super(message);
    }

    public SensitiveException(String message, Object... params) {
        super(String.format(message, params));
    }

    public SensitiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public SensitiveException(Throwable cause) {
        super(cause);
    }
}
