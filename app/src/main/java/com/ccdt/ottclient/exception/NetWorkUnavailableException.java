package com.ccdt.ottclient.exception;

public class NetWorkUnavailableException extends SuperException {

	private static final long serialVersionUID = -8264702393506648051L;

	public NetWorkUnavailableException(int code, String desc, String data) {
		errorCode = code;
		errorDesc = desc;
		jsonData = data;
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