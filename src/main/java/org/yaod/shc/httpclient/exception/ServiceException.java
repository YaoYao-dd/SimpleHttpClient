package org.yaod.shc.httpclient.exception;

/**
 * @author Yaod
 **/
public class ServiceException extends ShcException{
    public ServiceException(int httpcode, String message) {
        this(httpcode,message,null);
    }

    public ServiceException(int httpcode, String message, Throwable cause) {
        super(httpcode, message, cause);
    }
}
