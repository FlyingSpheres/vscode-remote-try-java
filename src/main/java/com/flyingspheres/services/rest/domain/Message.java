package com.flyingspheres.services.rest.domain;

public class Message {
    private String toUserId;
    private String fromUserId;
    private String message;
    private boolean encrypt;

    public static Message build(String toUser, String fromUser, String message, boolean encrypted) {
        Message m = new Message();
        m.setToUserId(toUser);
        m.setFromUserId(fromUser);
        m.setMessage(message);
        m.setEncrypt(encrypted);

        return m;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }
}