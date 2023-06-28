package com.wallee.android.till.sample.till.model;

public class ErrorCode {
    public static final ErrorCode ERR_UNDEFINED = new ErrorCode(-1, "VSD failed for undefined reasons");
    public static final ErrorCode ERR_EXCEPTION = new ErrorCode(-2, "VSD exception");
    public static final ErrorCode ERR_EMPTY_RESPONSE = new ErrorCode(-3, "Internal error - empty response");
    public static final ErrorCode ERR_PANTOKEN_FAILED = new ErrorCode(-4, "Failed to generate panToken with provided card");
    public static final ErrorCode ERR_CONNECTION_FAILED = new ErrorCode(-5, "Failed to connect to VPJ");
    public static final ErrorCode ERR_NOT_RESPONDING = new ErrorCode(-6, "VSD not responding");

    private int code;
    private String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return String.valueOf(code);
    }

    public String getMessage() {
        return message;
    }
}
