package edu.suda.ada.exception;

/**
 * Created by LiYang on 2016/11/23.
 */
public class InitializationException extends Exception {
    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
