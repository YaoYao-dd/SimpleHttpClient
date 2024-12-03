package org.yaod.shc.httpclient.exception;

import java.util.Objects;

/**
 * @author Yaod
 **/
public class PageNotFoundException extends ShcException{
    public PageNotFoundException(int httpcode,String message) {
        super(httpcode,message,null);
    }

    public PageNotFoundException(int httpcode,String message, Throwable cause) {
        super(httpcode,message, cause);
    }
}
