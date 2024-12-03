package org.yaod.shc.httpclient.exception;

/**
 * @author Yaod
 **/
public class UnauthorizedException extends ShcException{
    public UnauthorizedException(int httpcode, String message, Throwable cause) {
        super(httpcode,message, cause);
    }

    public UnauthorizedException(int httpcode, String message) {
        this(httpcode, message, null);
    }
}
