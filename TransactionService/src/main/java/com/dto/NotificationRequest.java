package com.dto;

public class NotificationRequest {
    private String toAccount;
    private String message;

    public NotificationRequest(String toAccount,String message) {
        this.message = message;
        this.toAccount = toAccount;
    }
    public String getToAccount() {
        return toAccount;
    }
    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}