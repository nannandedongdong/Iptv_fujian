package com.ccdt.ottclient.exception;

public class NoResponseException extends SuperException {

	private static final long serialVersionUID = -1206712908203245803L;

	public NoResponseException(int code, String desc, String json) {
		errorCode = code;
		errorDesc = desc;
		jsonData = json;
	}

	@Override
	public int getErrorCode() {
		// TODO Auto-generated method stub
		return errorCode;
	}

	@Override
	public String getErrorDesc() {
		// TODO Auto-generated method stub
		return errorDesc;
	}

	@Override
	public String getJsonData() {
		return jsonData;
	}
}