package org.yaod.shc.httpclient.exception;

import java.util.Objects;

/**
 * @author Yaod
 **/
public class ShcException extends  RuntimeException{
    final protected int httpcode;
    public ShcException(int httpcode, String message, Throwable cause) {
        super(message, cause);
        this.httpcode=httpcode;
    }

    public static ShcException parseHttpCode(int httpcode, String msg){
        if(httpcode==400){
            return new InvalidRequestException(httpcode,Objects.nonNull(msg)?msg:"invalid request.");
        }else if(httpcode==404){
            return new PageNotFoundException(httpcode,Objects.nonNull(msg)?msg:"page not found.");
        }else if(httpcode==401){
            return new UnauthorizedException(httpcode,Objects.nonNull(msg)?msg:"unauthorized access.");
        }else if(httpcode==403){
            return new ForbiddenException(httpcode,Objects.nonNull(msg)?msg:"access forbidden.");
        }else if(httpcode>=500 && httpcode <=599){
            return new ServiceException(httpcode,Objects.nonNull(msg)?msg:"server error.");
        }

        //we might need consider 2xx http code, but for now, only 4xx 5xx are considered.
        return null;
    }
}
