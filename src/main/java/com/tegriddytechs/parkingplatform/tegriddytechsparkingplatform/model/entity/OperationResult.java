package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

public class OperationResult {

    private boolean success;
    private String message;

    public OperationResult() {
    }

    public OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccessfull() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static OperationResult success(String msg) {
        return new OperationResult(true, msg);
    }

    public static OperationResult success(String msg, ParkingTicket ticket) {
        return new OperationResult(true, msg);
    }

    public static OperationResult failure(String msg) {
        return new OperationResult(false, msg);
    }

}
