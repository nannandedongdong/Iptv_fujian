package com.ccdt.ottclient.exception;

public class SuperExceptionFactory {

	public static final int ERROR_EXCEPTION_NOT_CUSTOM = -999; //异常未自定义
	public static final int ERROR_NETWORK_UNAVAILABLE = -1;//网络不可用
	public static final int ERROR_NO_RESPONSE = 0;//无响应

	
	public static final String DESC_ERROR_NETWORK_UNAVAILABLE = "网络不可用";
	public static final String DESC_ERROR_NO_RESPONSE = "服务器无响应";

	public static Exception create(int code, String desc, String jsonData) {
		switch (code) {
		case ERROR_NETWORK_UNAVAILABLE:
			return new NetWorkUnavailableException(code, desc,jsonData);
		case ERROR_NO_RESPONSE:
			return new NoResponseException(code, desc, jsonData);

		default:
			return new DefaultException(code,desc, jsonData);
		}
	}
}
