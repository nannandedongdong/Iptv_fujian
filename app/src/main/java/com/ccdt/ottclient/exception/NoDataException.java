package com.ccdt.ottclient.exception;

public class NoDataException extends SuperException {

    private static final long serialVersionUID = -6962420477060393314L;

    public NoDataException(int code, String desc, String json) {
        errorCode = code;
        errorDesc = desc;
        jsonData = json;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorDesc() {
        return errorDesc;
    }

    @Override
    public String getJsonData() {
        return jsonData;
    }
}
