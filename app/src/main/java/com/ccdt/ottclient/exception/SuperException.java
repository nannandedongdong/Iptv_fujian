package com.ccdt.ottclient.exception;

public abstract class SuperException extends Exception {


    private static final long serialVersionUID = 8146896138597223522L;
    protected int errorCode;
    protected String errorDesc;
    protected String jsonData;

    public abstract int getErrorCode();

    public abstract String getErrorDesc();

    public abstract String getJsonData();
}
