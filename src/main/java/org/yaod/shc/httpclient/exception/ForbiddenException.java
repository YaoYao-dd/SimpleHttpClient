package org.yaod.shc.httpclient.exception;

/**
 * @author Yaod
 **/
public class ForbiddenException extends ShcException{
    public ForbiddenException(int httpcode,String message, Throwable cause) {
        super(httpcode,message, cause);
    }

    public ForbiddenException(int httpcode,String message) {
        this(httpcode, message,null);
    }
}
