package com.qunzhi.yespmp.exception;


import com.qunzhi.yespmp.response.ResponseEnum;

public class TException extends AbstractException {

  private static final long serialVersionUID = 1L;

  private TException(ResponseEnum msg) {
    super(msg);
  }

  // client exceptions, others use of method to create

  public static TException badRequest() {
    return new TException(ResponseEnum.BAD_REQUEST);
  }

  public static AbstractException badRequest(String msg) {
    return new TException(ResponseEnum.BAD_REQUEST).with(msg);
  }

  public static TException forbidden() {
    return new TException(ResponseEnum.FORBIDDEN);
  }

  public static AbstractException forbidden(String msg) {
    return new TException(ResponseEnum.FORBIDDEN).with(msg);
  }

  public static TException unauthorized() {
    return new TException(ResponseEnum.UNAUTHORIZED);
  }

  public static TException notFound() {
    return new TException(ResponseEnum.NOT_FOUND);
  }

  public static TException tooManyRequests() {
    return new TException(ResponseEnum.TOO_MANY_REQUESTS);
  }

  public static TException of(ResponseEnum res) {
    return new TException(res);
  }


}
