package org.yaod.shc.httpclient.exception;

/**
 * @author Yaod
 **/
public class InvalidRequestException extends ShcException{
    public InvalidRequestException(int httpcode, String message) {
        this(httpcode,message,null);
    }

    public InvalidRequestException(int httpcode,String message, Throwable cause) {
        super(httpcode,message, cause);
    }
}
